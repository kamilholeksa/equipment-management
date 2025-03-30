package com.example.equipmentmanagement.controller;

import com.example.equipmentmanagement.auth.UserAuthProvider;
import com.example.equipmentmanagement.dto.CredentialsDto;
import com.example.equipmentmanagement.dto.UserAuthDto;
import com.example.equipmentmanagement.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class AuthController {

    private final AuthService authService;
    private final UserAuthProvider userAuthProvider;

    public AuthController(AuthService authService, UserAuthProvider userAuthProvider) {
        this.authService = authService;
        this.userAuthProvider = userAuthProvider;
    }

    @PostMapping("/login")
    ResponseEntity<UserAuthDto> login(@Valid @RequestBody CredentialsDto credentialsDto) {
        UserAuthDto user = authService.login(credentialsDto);
        user.setToken(userAuthProvider.createToken(user));
        return ResponseEntity.ok(user);
    }
}
