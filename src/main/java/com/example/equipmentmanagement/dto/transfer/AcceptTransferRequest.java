package com.example.equipmentmanagement.dto.transfer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AcceptTransferRequest {

    private Long equipmentId;

    @NotBlank(message = "Location is required")
    private String newLocation;

    @NotNull(message = "Address is required")
    private Long newAddressId;
}
