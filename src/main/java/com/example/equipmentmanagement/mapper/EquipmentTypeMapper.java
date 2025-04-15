package com.example.equipmentmanagement.mapper;

import com.example.equipmentmanagement.dto.equipment.EquipmentTypeDto;
import com.example.equipmentmanagement.dto.equipment.EquipmentTypeSaveDto;
import com.example.equipmentmanagement.model.EquipmentType;

public class EquipmentTypeMapper {

    private EquipmentTypeMapper() {
    }

    public static EquipmentTypeDto toDto(EquipmentType equipmentType) {
        EquipmentTypeDto dto = new EquipmentTypeDto();
        dto.setId(equipmentType.getId());
        dto.setName(equipmentType.getName());
        dto.setDescription(equipmentType.getDescription());

        return dto;
    }

    public static EquipmentType toEntity(EquipmentTypeSaveDto dto) {
        EquipmentType equipmentType = new EquipmentType();
        equipmentType.setName(dto.getName());
        equipmentType.setDescription(dto.getDescription());

        return equipmentType;
    }

}
