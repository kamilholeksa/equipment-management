package com.example.equipmentmanagement.dto.servicerequest;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ServiceRequestSaveDto {
    @NotBlank(message = "Temat zgłoszenia jest wymagany")
    private String title;
    private String description;
    private String status;
    private String closeInfo;
    private Long equipmentId;
    private Long userId;
    private Long technicianId;
}
