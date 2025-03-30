package com.example.equipmentmanagement.dto.mapper;

import com.example.equipmentmanagement.dto.EquipmentDto;
import com.example.equipmentmanagement.dto.EquipmentSaveDto;
import com.example.equipmentmanagement.model.Address;
import com.example.equipmentmanagement.model.Equipment;
import com.example.equipmentmanagement.model.EquipmentType;
import com.example.equipmentmanagement.model.User;
import com.example.equipmentmanagement.model.enumeration.EquipmentStatus;

public class EquipmentMapper {

    private EquipmentMapper() {
    }

    public static EquipmentDto toDto(Equipment equipment) {
        EquipmentDto dto = new EquipmentDto();
        dto.setId(equipment.getId());
        dto.setManufacturer(equipment.getManufacturer());
        dto.setModel(equipment.getModel());
        dto.setDescription(equipment.getDescription());
        dto.setInventoryNumber(equipment.getInventoryNumber());
        dto.setSerialNumber(equipment.getSerialNumber());
        dto.setStatus(equipment.getStatus().toString());
        dto.setLocation(equipment.getLocation());
        dto.setPurchaseDate(equipment.getPurchaseDate());
        dto.setWarrantyUntil(equipment.getWarrantyUntil());
        dto.setWithdrawalDate(equipment.getWithdrawalDate());
        dto.setCreatedBy(equipment.getCreatedBy());
        dto.setCreatedDate(equipment.getCreatedDate());
        dto.setLastModifiedBy(equipment.getLastModifiedBy());
        dto.setLastModifiedDate(equipment.getLastModifiedDate());

        if (equipment.getType() != null) {
            dto.setType(EquipmentTypeMapper.toDto(equipment.getType()));
        }

        if (equipment.getAddress() != null) {
            dto.setAddress(AddressMapper.toDto(equipment.getAddress()));
        }

        if (equipment.getUser() != null) {
            dto.setUser(UserMapper.toDto(equipment.getUser()));
        }

        return dto;
    }

    public static Equipment toEntity(EquipmentSaveDto dto, EquipmentType type, Address address, User user) {
        Equipment equipment = new Equipment();
        equipment.setManufacturer(dto.getManufacturer());
        equipment.setModel(dto.getModel());
        equipment.setDescription(dto.getDescription());
        equipment.setInventoryNumber(dto.getInventoryNumber());
        equipment.setSerialNumber(dto.getSerialNumber());
        equipment.setStatus(EquipmentStatus.valueOf(dto.getStatus()));
        equipment.setLocation(dto.getLocation());
        equipment.setPurchaseDate(dto.getPurchaseDate());
        equipment.setWarrantyUntil(dto.getWarrantyUntil());
        equipment.setWithdrawalDate(dto.getWithdrawalDate());
        equipment.setType(type);
        equipment.setAddress(address);
        equipment.setUser(user);

        return equipment;
    }
}
