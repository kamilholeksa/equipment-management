package com.example.equipmentmanagement.dto.servicerequest;

import com.example.equipmentmanagement.dto.equipment.EquipmentDto;
import com.example.equipmentmanagement.dto.user.UserDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.Instant;
import java.util.Set;

@Data
public class ServiceRequestWithNotesDto {
    private Long id;
    private String title;
    private String description;
    private String status;
    private String closeInfo;
    @JsonIgnoreProperties({"createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate"})
    private EquipmentDto equipment;
    @JsonIgnoreProperties({"active", "roles", "createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate"})
    private UserDto user;
    @JsonIgnoreProperties({"active", "roles", "createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate"})
    private UserDto technician;
    @JsonIgnoreProperties({"serviceRequest"})
    private Set<ServiceRequestNoteDto> notes;
    private String createdBy;
    private Instant createdDate;
    private String lastModifiedBy;
    private Instant lastModifiedDate;
}
