package com.example.equipmentmanagement.service;

import com.example.equipmentmanagement.dto.transfer.TransferDto;
import com.example.equipmentmanagement.dto.transfer.TransferSaveDto;
import com.example.equipmentmanagement.dto.user.UserDto;
import com.example.equipmentmanagement.mapper.TransferMapper;
import com.example.equipmentmanagement.dto.transfer.AcceptTransferRequest;
import com.example.equipmentmanagement.exception.ResourceNotFoundException;
import com.example.equipmentmanagement.model.Address;
import com.example.equipmentmanagement.model.Equipment;
import com.example.equipmentmanagement.model.Transfer;
import com.example.equipmentmanagement.model.User;
import com.example.equipmentmanagement.enumeration.EquipmentStatus;
import com.example.equipmentmanagement.enumeration.TransferStatus;
import com.example.equipmentmanagement.repository.AddressRepository;
import com.example.equipmentmanagement.repository.EquipmentRepository;
import com.example.equipmentmanagement.repository.TransferRepository;
import com.example.equipmentmanagement.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static com.example.equipmentmanagement.mapper.TransferMapper.toDto;
import static com.example.equipmentmanagement.mapper.TransferMapper.toEntity;

@Service
public class TransferService {

    @PersistenceContext
    private EntityManager entityManager;

    private final TransferRepository transferRepository;
    private final EquipmentRepository equipmentRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final UserService userService;
    private final EquipmentHistoryService equipmentHistoryService;

    public TransferService(TransferRepository transferRepository, EquipmentRepository equipmentRepository, UserRepository userRepository, AddressRepository addressRepository, UserService userService, EquipmentHistoryService equipmentHistoryService) {
        this.transferRepository = transferRepository;
        this.equipmentRepository = equipmentRepository;
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.userService = userService;
        this.equipmentHistoryService = equipmentHistoryService;
    }

    public Page<TransferDto> getAllTransfers(
            int pageNumber,
            int pageSize,
            String sortField,
            String sortOrder
    ) {
        Sort sort = Sort.by(sortOrder.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortField);
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        Page<Long> idsPage = transferRepository.findAllIds(pageRequest);
        Set<Long> ids = Set.copyOf(idsPage.getContent());

        List<TransferDto> transfers = transferRepository.findAllByIdIn(ids, pageRequest.getSort()).stream()
                .map(TransferMapper::toDto)
                .toList();

        return new PageImpl<>(transfers, pageRequest, idsPage.getTotalElements());
    }

    public Page<TransferDto> getTransfersToAccept(
            int pageNumber,
            int pageSize,
            String sortField,
            String sortOrder
    ) {
        UserDto user = userService.getCurrentUser();

        Sort sort = Sort.by(sortOrder.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortField);
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        return transferRepository.getTransfersByObtainerUsernameAndStatus(user.getUsername(), TransferStatus.PENDING, pageRequest).map(TransferMapper::toDto);
    }

    public Page<TransferDto> getMyTransfers(
            int pageNumber,
            int pageSize,
            String sortField,
            String sortOrder
    ) {
        UserDto user = userService.getCurrentUser();

        Sort sort = Sort.by(sortOrder.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortField);
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        return transferRepository.getTransfersByTransferorUsernameOrObtainerUsername(user.getUsername(), user.getUsername(), pageRequest).map(TransferMapper::toDto);
    }

    public TransferDto getTransfer(Long id) {
        return toDto(findTransferById(id));
    }

    @Transactional
    public TransferDto createTransfer(TransferSaveDto dto) {
        UserDto user = userService.getCurrentUser();

        dto.setRequestDate(LocalDate.now());
        dto.setStatus(TransferStatus.PENDING.toString());
        dto.setTransferorId(user.getId());

        Equipment equipment = findEquipmentById(dto.getEquipmentId());
        User transferor = findUserById(dto.getTransferorId());
        User obtainer = findUserById(dto.getObtainerId());

        return toDto(transferRepository.save(toEntity(dto, equipment, transferor, obtainer)));
    }

    @Transactional
    public TransferDto updateTransfer(Long id, TransferSaveDto dto) {
        Transfer existingTransfer = findTransferById(id);
        Equipment equipment = findEquipmentById(dto.getEquipmentId());
        User transferor = findUserById(dto.getTransferorId());
        User obtainer = findUserById(dto.getObtainerId());

        Transfer updatedTransfer = toEntity(dto, equipment, transferor, obtainer);
        updatedTransfer.setId(existingTransfer.getId());

        return toDto(transferRepository.save(updatedTransfer));
    }

    @Transactional
    public void deleteTransfer(Long id) {
        Transfer existingTransfer = findTransferById(id);
        transferRepository.delete(existingTransfer);
    }

    @Transactional
    public void acceptTransfer(Long id, AcceptTransferRequest request) {
        User user = userService.getCurrentUserEntity();
        Address address = findAddressById(request.getNewAddressId());

        // Get old state of equipment and detach it from entityManager
        Equipment oldEquipment = findEquipmentById(request.getEquipmentId());
        entityManager.detach(oldEquipment);

        // Get equipment and set new values to its changing fields
        Equipment equipment = findEquipmentById(request.getEquipmentId());
        equipment.setUser(user);
        equipment.setLocation(request.getNewLocation());
        equipment.setAddress(address);
        equipment.setStatus(EquipmentStatus.IN_USE);

        Transfer transfer = findTransferById(id);
        transfer.setStatus(TransferStatus.ACCEPTED);
        transfer.setDecisionDate(LocalDate.now());

        equipmentHistoryService.saveEquipmentHistory(oldEquipment, equipment);
    }

    @Transactional
    public void rejectTransfer(Long id) {
        Transfer transfer = findTransferById(id);
        transfer.setStatus(TransferStatus.REJECTED);
        transfer.setDecisionDate(LocalDate.now());

        transferRepository.save(transfer);
    }

    private Transfer findTransferById(Long id) {
        return transferRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transfer", id));
    }

    private Equipment findEquipmentById(Long id) {
        return equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment", id));
    }

    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
    }

    private Address findAddressById(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address", id));
    }
}
