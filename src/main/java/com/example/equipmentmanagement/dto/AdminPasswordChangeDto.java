package com.example.equipmentmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AdminPasswordChangeDto {

    @Size(min = 8, message = "Nowe hasło musi mieć min. 8 znaków")
    @NotBlank(message = "Nowe hasło nie może być puste")
    String newPassword;

    @NotBlank(message = "Potwierdzenie hasła nie może być puste")
    String confirmPassword;
}
