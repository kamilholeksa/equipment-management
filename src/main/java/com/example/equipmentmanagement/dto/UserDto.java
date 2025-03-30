package com.example.equipmentmanagement.dto;

import lombok.Data;

import java.time.Instant;
import java.util.Set;

@Data
public class UserDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String username;

    private String email;

    private String phoneNumber;

    private boolean active;

    private Set<String> roles;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

}
