package com.example.equipmentmanagement.mapper;

import com.example.equipmentmanagement.dto.address.AddressDto;
import com.example.equipmentmanagement.dto.address.AddressSaveDto;
import com.example.equipmentmanagement.model.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    AddressDto addressToAddressDto(Address address);

    @Mapping(target = "id", ignore = true)
    Address addressSaveDtoToAddress(AddressSaveDto dto);
}
