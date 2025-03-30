package com.example.equipmentmanagement.dto.mapper;

import com.example.equipmentmanagement.dto.EquipmentHistoryDto;
import com.example.equipmentmanagement.model.EquipmentHistory;

public class EquipmentHistoryMapper {

    private EquipmentHistoryMapper() {
    }

    public static EquipmentHistoryDto toDto(EquipmentHistory equipmentHistory) {
        EquipmentHistoryDto dto = new EquipmentHistoryDto();
        dto.setId(equipmentHistory.getId());
        dto.setOldStatus(equipmentHistory.getOldStatus());
        dto.setNewStatus(equipmentHistory.getNewStatus());
        dto.setOldLocation(equipmentHistory.getOldLocation());
        dto.setNewLocation(equipmentHistory.getNewLocation());
        dto.setChangeDate(equipmentHistory.getTimestamp());

        if (equipmentHistory.getEquipment() != null) {
            dto.setEquipment(EquipmentMapper.toDto(equipmentHistory.getEquipment()));
        }

        if (equipmentHistory.getOldUser() != null) {
            dto.setOldUser(UserMapper.toDto(equipmentHistory.getOldUser()));
        }

        if (equipmentHistory.getNewUser() != null) {
            dto.setNewUser(UserMapper.toDto(equipmentHistory.getNewUser()));
        }

        return dto;
    }
}
