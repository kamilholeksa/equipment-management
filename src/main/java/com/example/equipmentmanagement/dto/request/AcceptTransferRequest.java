package com.example.equipmentmanagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AcceptTransferRequest {

    private Long equipmentId;

    @NotBlank(message = "Lokalizacja jest wymagana")
    private String newLocation;

    @NotNull(message = "Adres jest wymagany")
    private Long newAddressId;
}
