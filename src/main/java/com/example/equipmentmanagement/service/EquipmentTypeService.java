package com.example.equipmentmanagement.service;

import com.example.equipmentmanagement.dto.EquipmentTypeDto;
import com.example.equipmentmanagement.dto.EquipmentTypeSaveDto;
import com.example.equipmentmanagement.dto.mapper.EquipmentTypeMapper;
import com.example.equipmentmanagement.exception.ResourceNotFoundException;
import com.example.equipmentmanagement.model.EquipmentType;
import com.example.equipmentmanagement.repository.EquipmentTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.equipmentmanagement.dto.mapper.EquipmentTypeMapper.toDto;
import static com.example.equipmentmanagement.dto.mapper.EquipmentTypeMapper.toEntity;

@Service
public class EquipmentTypeService {

    private final EquipmentTypeRepository equipmentTypeRepository;

    public EquipmentTypeService(EquipmentTypeRepository equipmentTypeRepository) {
        this.equipmentTypeRepository = equipmentTypeRepository;
    }

    public List<EquipmentTypeDto> getAllEquipmentTypes() {
        return this.equipmentTypeRepository.findAll().stream()
                .map(EquipmentTypeMapper::toDto)
                .toList();
    }

    public EquipmentTypeDto getEquipmentType(Long id) {
        return toDto(findEquipmentTypeById(id));
    }

    public EquipmentTypeDto createEquipmentType(EquipmentTypeSaveDto dto) {
        return toDto(this.equipmentTypeRepository.save(toEntity(dto)));
    }

    public EquipmentTypeDto updateEquipmentType(Long id, EquipmentTypeSaveDto dto) {
        EquipmentType existingEquipmentType = findEquipmentTypeById(id);
        EquipmentType updatedEquipmentType = toEntity(dto);

        updatedEquipmentType.setId(existingEquipmentType.getId());

        return toDto(equipmentTypeRepository.save(updatedEquipmentType));
    }

    public void deleteEquipmentType(Long id) {
        EquipmentType existingEquipmentType = findEquipmentTypeById(id);
        this.equipmentTypeRepository.delete(existingEquipmentType);
    }

    private EquipmentType findEquipmentTypeById(Long id) {
        return equipmentTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("EquipmentType", id));
    }
}
