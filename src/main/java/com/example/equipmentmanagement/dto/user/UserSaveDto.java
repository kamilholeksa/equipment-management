package com.example.equipmentmanagement.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class UserSaveDto {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Username is required")
    private String username;

    @Size(min = 8, message = "Password must contain at least 8 characters")
    private String password;

    @NotBlank(message = "Email address is required")
    @Email(message = "Email address has incorrect format")
    private String email;

    @Size(max = 20, message = "Phone number can have a maximum of 20 digits")
    private String phoneNumber;

    private boolean active;

    @NotEmpty(message = "At least one role is required")
    private Set<String> roles;
}
