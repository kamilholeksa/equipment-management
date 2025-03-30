package com.example.equipmentmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EquipmentTypeSaveDto {

    @NotBlank(message = "Nazwa jest wymagana")
    private String name;

    private String description;

}
