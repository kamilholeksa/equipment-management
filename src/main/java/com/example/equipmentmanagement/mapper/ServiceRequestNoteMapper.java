package com.example.equipmentmanagement.mapper;

import com.example.equipmentmanagement.dto.servicerequest.ServiceRequestNoteDto;
import com.example.equipmentmanagement.dto.servicerequest.ServiceRequestNoteSaveDto;
import com.example.equipmentmanagement.model.ServiceRequest;
import com.example.equipmentmanagement.model.ServiceRequestNote;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = ServiceRequestMapper.class)
public interface ServiceRequestNoteMapper {

    ServiceRequestNoteDto noteToNoteDto(ServiceRequestNote note);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "serviceRequest", expression = "java(mapServiceRequest(dto.getServiceRequestId()))")
    ServiceRequestNote noteSaveDtoToNote(ServiceRequestNoteSaveDto dto);

    default ServiceRequest mapServiceRequest(Long requestId) {
        if (requestId == null) return null;
        ServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setId(requestId);
        return serviceRequest;
    }
}
