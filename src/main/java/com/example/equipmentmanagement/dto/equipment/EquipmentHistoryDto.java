package com.example.equipmentmanagement.dto.equipment;

import com.example.equipmentmanagement.dto.user.UserDto;
import com.example.equipmentmanagement.enumeration.EquipmentStatus;
import lombok.Data;

import java.time.Instant;

@Data
public class EquipmentHistoryDto {
    private Long id;
    private EquipmentDto equipment;
    private EquipmentStatus oldStatus;
    private EquipmentStatus newStatus;
    private String oldLocation;
    private String newLocation;
    private UserDto oldUser;
    private UserDto newUser;
    private Instant changeDate;
}
