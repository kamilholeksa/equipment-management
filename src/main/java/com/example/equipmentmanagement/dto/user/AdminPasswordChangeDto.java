package com.example.equipmentmanagement.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AdminPasswordChangeDto {

    @Size(min = 8, message = "New password must contain at least 8 characters")
    @NotBlank(message = "New password cannot be blank")
    String newPassword;

    @NotBlank(message = "Password confirmation cannot be blank")
    String confirmPassword;
}
