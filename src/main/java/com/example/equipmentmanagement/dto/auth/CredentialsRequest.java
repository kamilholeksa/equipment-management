package com.example.equipmentmanagement.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CredentialsRequest {

    @NotBlank(message = "Nazwa użytkownika jest wymagana")
    String username;

    @NotBlank(message = "Hasło jest wymagane")
    String password;
}
