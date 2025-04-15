package com.example.equipmentmanagement.dto.servicerequest;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Data;

import java.time.Instant;

@Data
public class ServiceRequestNoteDto {
    private Long id;
    private String description;
    @JsonIncludeProperties("id")
    private ServiceRequestDto serviceRequest;
    private String createdBy;
    private Instant createdDate;
    private String lastModifiedBy;
    private Instant lastModifiedDate;
}
