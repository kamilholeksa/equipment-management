package com.example.equipmentmanagement.service;

import com.example.equipmentmanagement.dto.equipment.EquipmentTypeDto;
import com.example.equipmentmanagement.dto.equipment.EquipmentTypeSaveDto;
import com.example.equipmentmanagement.exception.BadRequestAlertException;
import com.example.equipmentmanagement.mapper.EquipmentTypeMapper;
import com.example.equipmentmanagement.exception.ResourceNotFoundException;
import com.example.equipmentmanagement.model.EquipmentType;
import com.example.equipmentmanagement.repository.EquipmentRepository;
import com.example.equipmentmanagement.repository.EquipmentTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipmentTypeService {

    private final EquipmentTypeRepository equipmentTypeRepository;
    private final EquipmentRepository equipmentRepository;
    private final EquipmentTypeMapper equipmentTypeMapper;

    public EquipmentTypeService(EquipmentTypeRepository equipmentTypeRepository, EquipmentRepository equipmentRepository, EquipmentTypeMapper equipmentTypeMapper) {
        this.equipmentTypeRepository = equipmentTypeRepository;
        this.equipmentRepository = equipmentRepository;
        this.equipmentTypeMapper = equipmentTypeMapper;
    }

    public List<EquipmentTypeDto> getAllEquipmentTypes() {
        return this.equipmentTypeRepository.findAll().stream()
                .map(equipmentTypeMapper::typeToTypeDto)
                .toList();
    }

    public EquipmentTypeDto getEquipmentType(Long id) {
        return equipmentTypeMapper.typeToTypeDto(findEquipmentTypeById(id));
    }

    public EquipmentTypeDto createEquipmentType(EquipmentTypeSaveDto dto) {
        EquipmentType type = equipmentTypeMapper.typeSaveDtoToType(dto);
        return equipmentTypeMapper.typeToTypeDto(this.equipmentTypeRepository.save(type));
    }

    public EquipmentTypeDto updateEquipmentType(Long id, EquipmentTypeSaveDto dto) {
        EquipmentType existingEquipmentType = findEquipmentTypeById(id);
        EquipmentType updatedEquipmentType = equipmentTypeMapper.typeSaveDtoToType(dto);

        updatedEquipmentType.setId(existingEquipmentType.getId());

        return equipmentTypeMapper.typeToTypeDto(equipmentTypeRepository.save(updatedEquipmentType));
    }

    public void deleteEquipmentType(Long id) {
        EquipmentType existingEquipmentType = findEquipmentTypeById(id);
        int count = equipmentRepository.countByTypeId(id);
        if (count > 0) {
            throw new BadRequestAlertException("Failed. There are equipment with this type assigned: " + count);
        }

        this.equipmentTypeRepository.delete(existingEquipmentType);
    }

    private EquipmentType findEquipmentTypeById(Long id) {
        return equipmentTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("EquipmentType", id));
    }
}
