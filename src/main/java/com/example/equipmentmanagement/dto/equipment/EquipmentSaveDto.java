package com.example.equipmentmanagement.dto.equipment;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EquipmentSaveDto {

    @NotBlank(message = "Manufacturer is required")
    private String manufacturer;

    @NotBlank(message = "Model is required")
    private String model;

    private String description;

    @NotBlank(message = "Inventory number is required")
    private String inventoryNumber;

    private String serialNumber;

    @NotBlank(message = "Status is required")
    private String status;

    private String location;

    private LocalDate purchaseDate;

    private LocalDate warrantyUntil;

    private LocalDate withdrawalDate;

    private Long typeId;

    private Long addressId;

    private Long userId;

}
