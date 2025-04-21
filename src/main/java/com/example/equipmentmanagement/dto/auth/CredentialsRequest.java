package com.example.equipmentmanagement.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CredentialsRequest {

    @NotBlank(message = "Username is required")
    String username;

    @NotBlank(message = "Password is required")
    String password;
}
