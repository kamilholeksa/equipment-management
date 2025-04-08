package com.example.equipmentmanagement.controller;

import com.example.equipmentmanagement.auth.UserAuthProvider;
import com.example.equipmentmanagement.dto.AccountDto;
import com.example.equipmentmanagement.dto.AuthResponseDto;
import com.example.equipmentmanagement.dto.CredentialsDto;
import com.example.equipmentmanagement.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final UserAuthProvider userAuthProvider;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public AuthController(UserAuthProvider userAuthProvider, AuthenticationManager authenticationManager, UserService userService) {
        this.userAuthProvider = userAuthProvider;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
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

    @GetMapping("/account")
    public ResponseEntity<AccountDto> getAccount() {
        return ResponseEntity.ok(userService.getCurrentUserAccount());
    }
}
