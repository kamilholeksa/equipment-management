package com.example.equipmentmanagement.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Data;

import java.time.Instant;

@Data
public class ServiceRequestDto {

    private Long id;

    private String title;

    private String description;

    private String status;

    private String closeInfo;

    @JsonIgnoreProperties({"type", "address", "user", "createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate"})
    private EquipmentDto equipment;

    @JsonIncludeProperties({"firstName", "lastName", "username"})
    private UserDto user;

    @JsonIncludeProperties({"firstName", "lastName", "username"})
    private UserDto technician;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;
}
