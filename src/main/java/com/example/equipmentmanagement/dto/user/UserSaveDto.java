package com.example.equipmentmanagement.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class UserSaveDto {

    @NotBlank(message = "Imię jest wymagane")
    private String firstName;

    @NotBlank(message = "Nazwisko jest wymagane")
    private String lastName;

    @NotBlank(message = "Nazwa użytkownika jest wymagana")
    private String username;

    @Size(min = 8, message = "Hasło musi mieć min. 8 znaków")
    private String password;

    @NotBlank(message = "Adres e-mail jest wymagany")
    @Email(message = "Adres e-mail jest niepoprawny")
    private String email;

    @Size(max = 20, message = "Numer telefonu może mieć maks. 20 znaków")
    private String phoneNumber;

    private boolean active;

    @NotEmpty(message = "Przynajmniej jedna rola jest wymagana")
    private Set<String> roles;
}
