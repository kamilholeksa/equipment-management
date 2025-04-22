package com.example.equipmentmanagement.mapper;

import com.example.equipmentmanagement.dto.equipment.EquipmentTypeDto;
import com.example.equipmentmanagement.dto.equipment.EquipmentTypeSaveDto;
import com.example.equipmentmanagement.model.EquipmentType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EquipmentTypeMapper {

    EquipmentTypeDto typeToTypeDto(EquipmentType equipmentType);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "equipmentSet", ignore = true)
    EquipmentType typeSaveDtoToType(EquipmentTypeSaveDto dto);
}
