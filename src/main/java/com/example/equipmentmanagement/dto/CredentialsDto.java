package com.example.equipmentmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CredentialsDto {

    @NotBlank(message = "Nazwa użytkownika jest wymagana")
    String username;

    @NotBlank(message = "Hasło jest wymagane")
    String password;

}
