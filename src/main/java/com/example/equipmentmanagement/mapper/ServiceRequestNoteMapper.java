package com.example.equipmentmanagement.mapper;

import com.example.equipmentmanagement.dto.servicerequest.ServiceRequestNoteDto;
import com.example.equipmentmanagement.dto.servicerequest.ServiceRequestNoteSaveDto;
import com.example.equipmentmanagement.model.ServiceRequest;
import com.example.equipmentmanagement.model.ServiceRequestNote;

public class ServiceRequestNoteMapper {

    private ServiceRequestNoteMapper() {
    }

    public static ServiceRequestNoteDto toDto(ServiceRequestNote serviceRequestNote) {
        ServiceRequestNoteDto dto = new ServiceRequestNoteDto();
        dto.setId(serviceRequestNote.getId());
        dto.setDescription(serviceRequestNote.getDescription());
        dto.setCreatedBy(serviceRequestNote.getCreatedBy());
        dto.setCreatedDate(serviceRequestNote.getCreatedDate());
        dto.setLastModifiedBy(serviceRequestNote.getLastModifiedBy());
        dto.setLastModifiedDate(serviceRequestNote.getLastModifiedDate());

        if (serviceRequestNote.getServiceRequest() != null) {
            dto.setServiceRequest(ServiceRequestMapper.toDto(serviceRequestNote.getServiceRequest()));
        }

        return dto;
    }

    public static ServiceRequestNote toEntity(ServiceRequestNoteSaveDto dto, ServiceRequest serviceRequest) {
        ServiceRequestNote serviceRequestNote = new ServiceRequestNote();
        serviceRequestNote.setDescription(dto.getDescription());
        serviceRequestNote.setServiceRequest(serviceRequest);

        return serviceRequestNote;
    }

}
