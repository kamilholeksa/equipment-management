package com.example.equipmentmanagement.mapper;

import com.example.equipmentmanagement.dto.servicerequest.ServiceRequestDto;
import com.example.equipmentmanagement.dto.servicerequest.ServiceRequestNoteDto;
import com.example.equipmentmanagement.dto.servicerequest.ServiceRequestSaveDto;
import com.example.equipmentmanagement.dto.servicerequest.ServiceRequestWithNotesDto;
import com.example.equipmentmanagement.model.Equipment;
import com.example.equipmentmanagement.model.ServiceRequest;
import com.example.equipmentmanagement.model.User;
import com.example.equipmentmanagement.enumeration.ServiceRequestStatus;

import java.util.Set;
import java.util.stream.Collectors;

public class ServiceRequestMapper {

    private ServiceRequestMapper() {
    }

    public static ServiceRequestDto toDto(ServiceRequest serviceRequest) {
        ServiceRequestDto dto = new ServiceRequestDto();
        dto.setId(serviceRequest.getId());
        dto.setTitle(serviceRequest.getTitle());
        dto.setDescription(serviceRequest.getDescription());
        dto.setStatus(serviceRequest.getStatus().toString());
        dto.setCloseInfo(serviceRequest.getCloseInfo());
        dto.setCreatedBy(serviceRequest.getCreatedBy());
        dto.setCreatedDate(serviceRequest.getCreatedDate());
        dto.setLastModifiedBy(serviceRequest.getLastModifiedBy());
        dto.setLastModifiedDate(serviceRequest.getLastModifiedDate());

        if (serviceRequest.getEquipment() != null) {
            dto.setEquipment(EquipmentMapper.toDto(serviceRequest.getEquipment()));
        }

        if (serviceRequest.getUser() != null) {
            dto.setUser(UserMapper.toDto(serviceRequest.getUser()));
        }

        if (serviceRequest.getTechnician() != null) {
            dto.setTechnician(UserMapper.toDto(serviceRequest.getTechnician()));
        }

        return dto;
    }

    public static ServiceRequestWithNotesDto toDtoWithNotes(ServiceRequest serviceRequest) {
        ServiceRequestWithNotesDto dto = new ServiceRequestWithNotesDto();
        dto.setId(serviceRequest.getId());
        dto.setTitle(serviceRequest.getTitle());
        dto.setDescription(serviceRequest.getDescription());
        dto.setStatus(serviceRequest.getStatus().toString());
        dto.setCloseInfo(serviceRequest.getCloseInfo());
        dto.setCreatedBy(serviceRequest.getCreatedBy());
        dto.setCreatedDate(serviceRequest.getCreatedDate());
        dto.setLastModifiedBy(serviceRequest.getLastModifiedBy());
        dto.setLastModifiedDate(serviceRequest.getLastModifiedDate());

        if (serviceRequest.getEquipment() != null) {
            dto.setEquipment(EquipmentMapper.toDto(serviceRequest.getEquipment()));
        }

        if (serviceRequest.getUser() != null) {
            dto.setUser(UserMapper.toDto(serviceRequest.getUser()));
        }

        if (serviceRequest.getTechnician() != null) {
            dto.setTechnician(UserMapper.toDto(serviceRequest.getTechnician()));
        }

        if (!serviceRequest.getNoteSet().isEmpty()) {
            Set<ServiceRequestNoteDto> notes = serviceRequest.getNoteSet().stream()
                    .map(ServiceRequestNoteMapper::toDto)
                    .collect(Collectors.toSet());

            dto.setNotes(notes);
        }

        return dto;
    }

    public static ServiceRequest toEntity(ServiceRequestSaveDto dto, Equipment equipment, User user, User technician) {
        ServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setTitle(dto.getTitle());
        serviceRequest.setDescription(dto.getDescription());
        serviceRequest.setStatus(ServiceRequestStatus.valueOf(dto.getStatus()));
        serviceRequest.setCloseInfo(dto.getCloseInfo());
        serviceRequest.setEquipment(equipment);
        serviceRequest.setUser(user);
        serviceRequest.setTechnician(technician);

        return serviceRequest;
    }

}
