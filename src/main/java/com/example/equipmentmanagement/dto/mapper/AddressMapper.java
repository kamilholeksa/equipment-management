package com.example.equipmentmanagement.dto.mapper;

import com.example.equipmentmanagement.dto.AddressDto;
import com.example.equipmentmanagement.dto.AddressSaveDto;
import com.example.equipmentmanagement.model.Address;

public class AddressMapper {

    private AddressMapper() {
    }

    public static AddressDto toDto(Address address) {
        AddressDto dto = new AddressDto();
        dto.setId(address.getId());
        dto.setPostalCode(address.getPostalCode());
        dto.setCity(address.getCity());
        dto.setStreet(address.getStreet());
        dto.setNumber(address.getNumber());
        dto.setDescription(address.getDescription());

        return dto;
    }

    public static Address toEntity(AddressSaveDto dto) {
        Address address = new Address();
        address.setPostalCode(dto.getPostalCode());
        address.setCity(dto.getCity());
        address.setStreet(dto.getStreet());
        address.setNumber(dto.getNumber());
        address.setDescription(dto.getDescription());

        return address;
    }

}
