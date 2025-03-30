package com.example.equipmentmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddressSaveDto {

    @NotBlank(message = "Kod pocztowy jest wymagany")
    private String postalCode;

    @NotBlank(message = "Miasto jest wymagane")
    private String city;

    @NotBlank(message = "Ulica jest wymagana")
    private String street;

    @NotBlank(message = "Numer jest wymagany")
    private String number;

    private String description;
}
