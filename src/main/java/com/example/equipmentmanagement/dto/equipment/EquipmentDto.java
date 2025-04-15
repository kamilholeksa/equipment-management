package com.example.equipmentmanagement.dto.equipment;

import com.example.equipmentmanagement.dto.address.AddressDto;
import com.example.equipmentmanagement.dto.user.UserDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;

@Data
public class EquipmentDto {
    private Long id;
    private String manufacturer;
    private String model;
    private String description;
    private String inventoryNumber;
    private String serialNumber;
    private String status;
    private String location;
    private LocalDate purchaseDate;
    private LocalDate warrantyUntil;
    private LocalDate withdrawalDate;
    @JsonIgnoreProperties("description")
    private EquipmentTypeDto type;
    private AddressDto address;
    @JsonIncludeProperties({"firstName", "lastName", "username"})
    private UserDto user;
    private String createdBy;
    private Instant createdDate;
    private String lastModifiedBy;
    private Instant lastModifiedDate;
}