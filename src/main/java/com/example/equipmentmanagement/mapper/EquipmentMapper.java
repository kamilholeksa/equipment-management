package com.example.equipmentmanagement.mapper;

import com.example.equipmentmanagement.dto.equipment.EquipmentDto;
import com.example.equipmentmanagement.dto.equipment.EquipmentSaveDto;
import com.example.equipmentmanagement.model.Address;
import com.example.equipmentmanagement.model.Equipment;
import com.example.equipmentmanagement.model.EquipmentType;
import com.example.equipmentmanagement.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class, AddressMapper.class, EquipmentTypeMapper.class})
public interface EquipmentMapper {

    EquipmentDto equipmentToEquipmentDto(Equipment equipment);

    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "address", expression = "java(mapAddress(dto.getAddressId()))")
    @Mapping(target = "user", expression = "java(mapUser(dto.getUserId()))")
    @Mapping(target = "type", expression = "java(mapEquipmentType(dto.getTypeId()))")
    Equipment equipmentSaveDtoToEquipment(EquipmentSaveDto dto);

    default User mapUser(Long userId) {
        if (userId == null) return null;
        User user = new User();
        user.setId(userId);
        return user;
    }

    default Address mapAddress(Long addressId) {
        if (addressId == null) return null;
        Address address = new Address();
        address.setId(addressId);
        return address;
    }

    default EquipmentType mapEquipmentType(Long typeId) {
        if (typeId == null) return null;
        EquipmentType type = new EquipmentType();
        type.setId(typeId);
        return type;
    }
}
