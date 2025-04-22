package com.example.equipmentmanagement.dto.servicerequest;

import lombok.Data;

import java.util.Set;

@Data
public class ServiceRequestFilter {
    private Long id;
    private String title;
    private Set<String> status;
    private Long userId;
    private Long technicianId;
}
