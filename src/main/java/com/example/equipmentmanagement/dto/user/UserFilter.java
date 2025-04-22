package com.example.equipmentmanagement.dto.user;

import lombok.Data;

import java.util.Set;

@Data
public class UserFilter {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String phoneNumber;
    private Set<String> roles;
    private Boolean active;
}
