package com.example.equipmentmanagement.dto.equipment;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EquipmentSaveDto {

    @NotBlank(message = "Producent jest wymagany")
    private String manufacturer;

    @NotBlank(message = "Model jest wymagany")
    private String model;

    private String description;

    @NotBlank(message = "Numer inwentarzowy jest wymagany")
    private String inventoryNumber;

    private String serialNumber;

    @NotBlank(message = "Status jest wymagany")
    private String status;

    private String location;

    private LocalDate purchaseDate;

    private LocalDate warrantyUntil;

    private LocalDate withdrawalDate;

    private Long typeId;

    private Long addressId;

    private Long userId;

}
