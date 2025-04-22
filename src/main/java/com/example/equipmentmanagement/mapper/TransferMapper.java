package com.example.equipmentmanagement.mapper;

import com.example.equipmentmanagement.dto.transfer.TransferDto;
import com.example.equipmentmanagement.dto.transfer.TransferSaveDto;
import com.example.equipmentmanagement.model.Equipment;
import com.example.equipmentmanagement.model.Transfer;
import com.example.equipmentmanagement.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {EquipmentMapper.class, UserMapper.class})
public interface TransferMapper {

    TransferDto transferToTransferDto(Transfer transfer);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "equipment", expression = "java(mapEquipment(dto.getEquipmentId()))")
    @Mapping(target = "transferor", expression = "java(mapUser(dto.getTransferorId()))")
    @Mapping(target = "obtainer", expression = "java(mapUser(dto.getObtainerId()))")
    Transfer transferSaveDtoToTransfer(TransferSaveDto dto);

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
