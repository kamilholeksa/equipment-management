package com.example.equipmentmanagement.service;

import com.example.equipmentmanagement.dto.equipment.EquipmentDto;
import com.example.equipmentmanagement.dto.equipment.EquipmentFilter;
import com.example.equipmentmanagement.dto.equipment.EquipmentSaveDto;
import com.example.equipmentmanagement.enumeration.EquipmentStatus;
import com.example.equipmentmanagement.exception.ResourceNotFoundException;
import com.example.equipmentmanagement.mapper.EquipmentMapper;
import com.example.equipmentmanagement.model.Equipment;
import com.example.equipmentmanagement.model.User;
import com.example.equipmentmanagement.repository.EquipmentRepository;
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
import java.util.HashSet;
import java.util.Set;

@Service
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final EquipmentMapper equipmentMapper;
    private final EquipmentHistoryService equipmentHistoryService;

    public EquipmentService(EquipmentRepository equipmentRepository, EquipmentMapper equipmentMapper, EquipmentHistoryService equipmentHistoryService) {
        this.equipmentRepository = equipmentRepository;
        this.equipmentMapper = equipmentMapper;
        this.equipmentHistoryService = equipmentHistoryService;
    }

    public Page<EquipmentDto> getAllEquipment(EquipmentFilter filter, Pageable pageable) {
        Specification<Equipment> spec = EquipmentSpecification.prepareSpecification(filter);
        return equipmentRepository.findAll(spec, pageable).map(equipmentMapper::equipmentToEquipmentDto);
    }

    public Page<EquipmentDto> getCurrentUserEquipment(EquipmentFilter filter, Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        Set<Long> users = new HashSet<>();
        users.add(user.getId());

        filter.setUserId(users);
        Specification<Equipment> spec = EquipmentSpecification.prepareSpecification(filter);

        return equipmentRepository.findAll(spec, pageable).map(equipmentMapper::equipmentToEquipmentDto);
    }

    public EquipmentDto getEquipment(Long id) {
        return equipmentMapper.equipmentToEquipmentDto(findEquipmentById(id));
    }

    @Transactional
    public EquipmentDto createEquipment(EquipmentSaveDto dto) {
        Equipment newEquipment = prepareAndValidateEquipment(dto, null);
        return equipmentMapper.equipmentToEquipmentDto(equipmentRepository.save(newEquipment));
    }

    @Transactional
    public EquipmentDto updateEquipment(Long id, EquipmentSaveDto dto) {
        Equipment existingEquipment = findEquipmentById(id);

        Equipment updatedEquipment = prepareAndValidateEquipment(dto, existingEquipment);
        equipmentHistoryService.saveEquipmentHistory(existingEquipment, updatedEquipment);

        return equipmentMapper.equipmentToEquipmentDto(equipmentRepository.save(updatedEquipment));
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

        Equipment equipment = equipmentMapper.equipmentSaveDtoToEquipment(dto);

        if (existingEquipment != null) {
            equipment.setId(existingEquipment.getId());
        } else {
            equipment.setStatus(EquipmentStatus.NEW);
        }

        return equipment;
    }
}
