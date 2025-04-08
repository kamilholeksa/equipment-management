package com.example.equipmentmanagement.service;

import com.example.equipmentmanagement.dto.EquipmentDto;
import com.example.equipmentmanagement.dto.EquipmentSaveDto;
import com.example.equipmentmanagement.dto.UserAuthDto;
import com.example.equipmentmanagement.dto.mapper.EquipmentMapper;
import com.example.equipmentmanagement.exception.ResourceNotFoundException;
import com.example.equipmentmanagement.model.Address;
import com.example.equipmentmanagement.model.Equipment;
import com.example.equipmentmanagement.model.EquipmentType;
import com.example.equipmentmanagement.model.User;
import com.example.equipmentmanagement.model.enumeration.EquipmentStatus;
import com.example.equipmentmanagement.repository.AddressRepository;
import com.example.equipmentmanagement.repository.EquipmentRepository;
import com.example.equipmentmanagement.repository.EquipmentTypeRepository;
import com.example.equipmentmanagement.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static com.example.equipmentmanagement.dto.mapper.EquipmentMapper.toDto;
import static com.example.equipmentmanagement.dto.mapper.EquipmentMapper.toEntity;

@Service
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final EquipmentTypeRepository equipmentTypeRepository;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final EquipmentHistoryService equipmentHistoryService;

    public EquipmentService(EquipmentRepository equipmentRepository, EquipmentTypeRepository equipmentTypeRepository, AddressRepository addressRepository, UserRepository userRepository, EquipmentHistoryService equipmentHistoryService) {
        this.equipmentRepository = equipmentRepository;
        this.equipmentTypeRepository = equipmentTypeRepository;
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.equipmentHistoryService = equipmentHistoryService;
    }

    public Page<EquipmentDto> getAllEquipment(
            int pageNumber,
            int pageSize,
            String sortField,
            String sortOrder
    ) {
        Sort sort = Sort.by(sortOrder.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortField);
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        Page<Long> idsPage = equipmentRepository.findAllIds(pageRequest);
        Set<Long> ids = Set.copyOf(idsPage.getContent());

        List<EquipmentDto> equipment = equipmentRepository.findAllByIdIn(ids, pageRequest.getSort()).stream()
                .map(EquipmentMapper::toDto)
                .toList();

        return new PageImpl<>(equipment, pageRequest, idsPage.getTotalElements());
    }

    public Page<EquipmentDto> getCurrentUserEquipment(
            int pageNumber,
            int pageSize,
            String sortField,
            String sortOrder
    ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Sort sort = Sort.by(sortOrder.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortField);
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        return equipmentRepository.findAllByUserUsername(username, pageRequest).map(EquipmentMapper::toDto);
    }

    public EquipmentDto getEquipment(Long id) {
        return toDto(findEquipmentById(id));
    }

    @Transactional
    public EquipmentDto createEquipment(EquipmentSaveDto dto) {
        Equipment newEquipment = prepareAndValidateEquipment(dto, null);
        return toDto(equipmentRepository.save(newEquipment));
    }

    @Transactional
    public EquipmentDto updateEquipment(Long id, EquipmentSaveDto dto) {
        Equipment existingEquipment = findEquipmentById(id);

        Equipment updatedEquipment = prepareAndValidateEquipment(dto, existingEquipment);
        equipmentHistoryService.saveEquipmentHistory(existingEquipment, updatedEquipment);

        return toDto(equipmentRepository.save(updatedEquipment));
    }

    @Transactional
    public void deleteEquipment(Long id) {
        Equipment existingEquipment = findEquipmentById(id);
        equipmentRepository.delete(existingEquipment);
    }

    @Transactional
    public void decommission(Long id) {
        Equipment equipment = findEquipmentById(id);
        equipment.setStatus(EquipmentStatus.DECOMMISSIONED);
        equipment.setWithdrawalDate(LocalDate.now());
    }

    private Equipment findEquipmentById(Long id) {
        return equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment", id));
    }

    private EquipmentType findEquipmentTypeById(Long id) {
        return equipmentTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Type", id));
    }

    private Address findAddressById(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address", id));
    }

    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
    }

    private Equipment prepareAndValidateEquipment(EquipmentSaveDto dto, Equipment existingEquipment) {
        dto.setSerialNumber(dto.getSerialNumber() != null && dto.getSerialNumber().isEmpty() ? null : dto.getSerialNumber());

        if (dto.getSerialNumber() != null &&
                (existingEquipment == null || !dto.getSerialNumber().equals(existingEquipment.getSerialNumber())) &&
                equipmentRepository.existsBySerialNumber(dto.getSerialNumber())) {
            throw new ValidationException(String.format("Equipment with serial number \"%s\" already exists", dto.getSerialNumber()));
        }

        if ((existingEquipment == null || !dto.getInventoryNumber().equals(existingEquipment.getInventoryNumber())) &&
                equipmentRepository.existsByInventoryNumber(dto.getInventoryNumber())) {
            throw new ValidationException(String.format("Equipment with inventory number \"%s\" already exists", dto.getInventoryNumber()));
        }

        EquipmentType type = dto.getTypeId() != null ? findEquipmentTypeById(dto.getTypeId()) : null;
        Address address = dto.getAddressId() != null ? findAddressById(dto.getAddressId()) : null;
        User user = dto.getUserId() != null ? findUserById(dto.getUserId()) : null;

        Equipment equipment = toEntity(dto, type, address, user);

        if (existingEquipment != null) {
            equipment.setId(existingEquipment.getId());
        } else {
            equipment.setStatus(EquipmentStatus.NEW);
        }

        return equipment;
    }
}
