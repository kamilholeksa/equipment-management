package com.example.equipmentmanagement.mapper;

import com.example.equipmentmanagement.dto.servicerequest.ServiceRequestDto;
import com.example.equipmentmanagement.dto.servicerequest.ServiceRequestSaveDto;
import com.example.equipmentmanagement.dto.servicerequest.ServiceRequestWithNotesDto;
import com.example.equipmentmanagement.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {EquipmentMapper.class, UserMapper.class})
public interface ServiceRequestMapper {

    ServiceRequestDto serviceRequestToServiceRequestDto(ServiceRequest serviceRequest);

    @Mapping(target = "notes", source = "noteSet")
    ServiceRequestWithNotesDto serviceRequestToServiceRequestWithNotesDto(ServiceRequest serviceRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "noteSet", ignore = true)
    @Mapping(target = "equipment", expression = "java(mapEquipment(dto.getEquipmentId()))")
    @Mapping(target = "user", expression = "java(mapUser(dto.getUserId()))")
    @Mapping(target = "technician", expression = "java(mapUser(dto.getTechnicianId()))")
    ServiceRequest serviceRequestSaveDtoToServiceRequest(ServiceRequestSaveDto dto);

    default Equipment mapEquipment(Long equipmentId) {
        if (equipmentId == null) return null;
        Equipment equipment = new Equipment();
        equipment.setId(equipmentId);
        return equipment;
    }

    default User mapUser(Long userId) {
        if (userId == null) return null;
        User user = new User();
        user.setId(userId);
        return user;
    }
}
