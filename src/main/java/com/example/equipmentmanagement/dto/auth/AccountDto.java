package com.example.equipmentmanagement.dto.auth;

import lombok.Data;

import java.util.Set;

@Data
public class AccountDto {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private boolean active;
    private Set<String> roles;
}
