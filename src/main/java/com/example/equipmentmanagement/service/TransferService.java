package com.example.equipmentmanagement.service;

import com.example.equipmentmanagement.dto.transfer.AcceptTransferRequest;
import com.example.equipmentmanagement.dto.transfer.TransferDto;
import com.example.equipmentmanagement.dto.transfer.TransferSaveDto;
import com.example.equipmentmanagement.dto.user.UserDto;
import com.example.equipmentmanagement.enumeration.EquipmentStatus;
import com.example.equipmentmanagement.enumeration.TransferStatus;
import com.example.equipmentmanagement.exception.ResourceNotFoundException;
import com.example.equipmentmanagement.mapper.TransferMapper;
import com.example.equipmentmanagement.model.Address;
import com.example.equipmentmanagement.model.Equipment;
import com.example.equipmentmanagement.model.Transfer;
import com.example.equipmentmanagement.model.User;
import com.example.equipmentmanagement.repository.AddressRepository;
import com.example.equipmentmanagement.repository.EquipmentRepository;
import com.example.equipmentmanagement.repository.TransferRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class TransferService {

    @PersistenceContext
    private EntityManager entityManager;

    private final TransferRepository transferRepository;
    private final TransferMapper transferMapper;
    private final EquipmentRepository equipmentRepository;
    private final AddressRepository addressRepository;
    private final UserService userService;
    private final EquipmentHistoryService equipmentHistoryService;

    public TransferService(TransferRepository transferRepository, TransferMapper transferMapper, EquipmentRepository equipmentRepository, AddressRepository addressRepository, UserService userService, EquipmentHistoryService equipmentHistoryService) {
        this.transferRepository = transferRepository;
        this.transferMapper = transferMapper;
        this.equipmentRepository = equipmentRepository;
        this.addressRepository = addressRepository;
        this.userService = userService;
        this.equipmentHistoryService = equipmentHistoryService;
    }

    public Page<TransferDto> getAllTransfers(Pageable pageable) {
        return transferRepository.findAll(pageable).map(transferMapper::transferToTransferDto);
    }

    public Page<TransferDto> getTransfersToAccept(Pageable pageable) {
        UserDto user = userService.getCurrentUser();
        return transferRepository.getTransfersByObtainerUsernameAndStatus(user.getUsername(), TransferStatus.PENDING, pageable).map(transferMapper::transferToTransferDto);
    }

    public Page<TransferDto> getMyTransfers(Pageable pageable) {
        UserDto user = userService.getCurrentUser();
        return transferRepository.getTransfersByTransferorUsernameOrObtainerUsername(user.getUsername(), user.getUsername(), pageable).map(transferMapper::transferToTransferDto);
    }

    public TransferDto getTransfer(Long id) {
        return transferMapper.transferToTransferDto(findTransferById(id));
    }

    @Transactional
    public TransferDto createTransfer(TransferSaveDto dto) {
        UserDto user = userService.getCurrentUser();

        dto.setRequestDate(LocalDate.now());
        dto.setStatus(TransferStatus.PENDING.toString());
        dto.setTransferorId(user.getId());

        Transfer transfer = transferMapper.transferSaveDtoToTransfer(dto);

        return transferMapper.transferToTransferDto(transferRepository.save(transfer));
    }

    @Transactional
    public TransferDto updateTransfer(Long id, TransferSaveDto dto) {
        Transfer existingTransfer = findTransferById(id);
        Transfer updatedTransfer = transferMapper.transferSaveDtoToTransfer(dto);
        updatedTransfer.setId(existingTransfer.getId());

        return transferMapper.transferToTransferDto(transferRepository.save(updatedTransfer));
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

    private Address findAddressById(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address", id));
    }
}
