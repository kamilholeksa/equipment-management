package com.example.equipmentmanagement.mapper;

import com.example.equipmentmanagement.dto.equipment.EquipmentHistoryDto;
import com.example.equipmentmanagement.model.EquipmentHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {EquipmentMapper.class, UserMapper.class})
public interface EquipmentHistoryMapper {

    @Mapping(target = "changeDate", source = "timestamp")
    EquipmentHistoryDto historyToHistoryDto(EquipmentHistory history);
}
