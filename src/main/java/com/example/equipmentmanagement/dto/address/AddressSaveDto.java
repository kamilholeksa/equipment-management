package com.example.equipmentmanagement.dto.address;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddressSaveDto {

    @NotBlank(message = "Postal code is required")
    private String postalCode;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "Street is required")
    private String street;

    @NotBlank(message = "Number is required")
    private String number;

    private String description;
}
