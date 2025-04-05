package com.example.equipmentmanagement.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserAuthDto {

    private String firstName;

    private String lastName;

    private String username;

    private String email;

    private List<String> roles;

    private String token;

    private String refreshToken;

}
