package com.example.equipmentmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CredentialsDto {

    @NotBlank(message = "Nazwa użytkownika jest wymagana")
    String username;

    @NotEmpty(message = "Hasło jest wymagane")
    char[] password;

}
