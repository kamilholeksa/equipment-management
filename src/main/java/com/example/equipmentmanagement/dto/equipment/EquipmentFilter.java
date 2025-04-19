package com.example.equipmentmanagement.dto.equipment;

import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class EquipmentFilter {
    private Long id;
    private String manufacturer;
    private String model;
    private String inventoryNumber;
    private String serialNumber;
    private Set<String> status;
    private String location;
    private LocalDate purchaseDate;
    private LocalDate warrantyUntil;
    private LocalDate withdrawalDate;
    private Long typeId;
    private Long addressId;
    private Set<Long> userId;
}
