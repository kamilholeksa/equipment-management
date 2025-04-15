package com.example.equipmentmanagement.dto.address;

import lombok.Data;

@Data
public class AddressDto {
    private Long id;
    private String postalCode;
    private String city;
    private String street;
    private String number;
    private String description;
}
