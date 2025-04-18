package com.example.equipmentmanagement.service;

import com.example.equipmentmanagement.dto.equipment.EquipmentDto;
import com.example.equipmentmanagement.dto.equipment.EquipmentFilter;
import com.example.equipmentmanagement.dto.equipment.EquipmentSaveDto;
import com.example.equipmentmanagement.enumeration.EquipmentStatus;
import com.example.equipmentmanagement.exception.ResourceNotFoundException;
import com.example.equipmentmanagement.mapper.EquipmentMapper;
import com.example.equipmentmanagement.model.Address;
import com.example.equipmentmanagement.model.Equipment;
import com.example.equipmentmanagement.model.EquipmentType;
import com.example.equipmentmanagement.model.User;
import com.example.equipmentmanagement.repository.AddressRepository;
import com.example.equipmentmanagement.repository.EquipmentRepository;
import com.example.equipmentmanagement.repository.EquipmentTypeRepository;
import com.example.equipmentmanagement.repository.UserRepository;
import com.example.equipmentmanagement.specification.EquipmentSpecification;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static com.example.equipmentmanagement.mapper.EquipmentMapper.toDto;
import static com.example.equipmentmanagement.mapper.EquipmentMapper.toEntity;

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

    public Page<EquipmentDto> getAllEquipment(EquipmentFilter filter, Pageable pageable) {
        Specification<Equipment> spec = EquipmentSpecification.prepareSpecification(filter);
        return equipmentRepository.findAll(spec, pageable).map(EquipmentMapper::toDto);
    }

    public Page<EquipmentDto> getCurrentUserEquipment(EquipmentFilter filter, Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        filter.setUserId(user.getId());
        Specification<Equipment> spec = EquipmentSpecification.prepareSpecification(filter);

        return equipmentRepository.findAll(spec, pageable).map(EquipmentMapper::toDto);
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
