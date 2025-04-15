package com.example.equipmentmanagement.dto.transfer;

import com.example.equipmentmanagement.dto.user.UserDto;
import com.example.equipmentmanagement.dto.equipment.EquipmentDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;

@Data
public class TransferDto {
    private Long id;
    private LocalDate requestDate;
    private LocalDate decisionDate;
    private String status;
    @JsonIgnoreProperties({"type", "address", "user", "createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate"})
    private EquipmentDto equipment;
    @JsonIncludeProperties({"firstName", "lastName", "username"})
    private UserDto transferor;
    @JsonIncludeProperties({"firstName", "lastName", "username"})
    private UserDto obtainer;
    private String createdBy;
    private Instant createdDate;
    private String lastModifiedBy;
    private Instant lastModifiedDate;
}
