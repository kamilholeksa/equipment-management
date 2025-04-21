package com.example.equipmentmanagement.dto.servicerequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ServiceRequestNoteSaveDto {

    @NotBlank(message = "Note content is required")
    private String description;

    @NotNull
    private Long serviceRequestId;
}
