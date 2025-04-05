package com.example.equipmentmanagement.controller;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.equipmentmanagement.auth.UserAuthProvider;
import com.example.equipmentmanagement.dto.AuthResponseDto;
import com.example.equipmentmanagement.dto.CredentialsDto;
import com.example.equipmentmanagement.dto.UserAuthDto;
import com.example.equipmentmanagement.service.AuthService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final UserAuthProvider userAuthProvider;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserAuthProvider userAuthProvider, AuthenticationManager authenticationManager) {
        this.userAuthProvider = userAuthProvider;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    ResponseEntity<AuthResponseDto> login(@Valid @RequestBody CredentialsDto credentialsDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        credentialsDto.getUsername(),
                        credentialsDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = userAuthProvider.createToken(authentication);
        String refreshToken = userAuthProvider.createRefreshToken(authentication);

        return ResponseEntity.ok(new AuthResponseDto(accessToken, refreshToken));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponseDto> refreshToken(@RequestBody String refreshToken) {
        if (userAuthProvider.isTokenValid(refreshToken)) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String newAccessToken = userAuthProvider.createToken(authentication);

            return ResponseEntity.ok(new AuthResponseDto(newAccessToken, refreshToken));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
