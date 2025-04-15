package com.example.equipmentmanagement.controller;

import com.example.equipmentmanagement.dto.auth.AccountDto;
import com.example.equipmentmanagement.dto.auth.AuthResponse;
import com.example.equipmentmanagement.dto.auth.CredentialsRequest;
import com.example.equipmentmanagement.dto.auth.RefreshTokenRequest;
import com.example.equipmentmanagement.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    ResponseEntity<AuthResponse> login(@Valid @RequestBody CredentialsRequest credentialsRequest) {
        return ResponseEntity.ok(
                authService.login(credentialsRequest));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(
                authService.refreshToken(request.getRefreshToken()));
    }

    @GetMapping("/account")
    public ResponseEntity<AccountDto> getAccount() {
        return ResponseEntity.ok(authService.getAccount());
    }
}
