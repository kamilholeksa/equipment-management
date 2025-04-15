package com.example.equipmentmanagement.service;

import com.example.equipmentmanagement.auth.UserAuthProvider;
import com.example.equipmentmanagement.dto.auth.AccountDto;
import com.example.equipmentmanagement.dto.UserDto;
import com.example.equipmentmanagement.dto.auth.AuthResponse;
import com.example.equipmentmanagement.dto.auth.CredentialsRequest;
import com.example.equipmentmanagement.exception.InvalidPasswordException;
import com.example.equipmentmanagement.exception.UnauthorizedException;
import com.example.equipmentmanagement.exception.UserNotFoundException;
import com.example.equipmentmanagement.model.User;
import com.example.equipmentmanagement.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserAuthProvider userAuthProvider;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, UserAuthProvider userAuthProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userAuthProvider = userAuthProvider;
    }

    public AuthResponse login(CredentialsRequest credentialsRequest) {
        User user = userRepository.findByUsername(credentialsRequest.getUsername())
                .orElseThrow(() -> new UserNotFoundException(credentialsRequest.getUsername()));

        if (!user.isActive()) {
            throw new UserNotFoundException(user.getUsername());
        }
        if (!passwordEncoder.matches(credentialsRequest.getPassword(), user.getPassword())) {
            throw new InvalidPasswordException();
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        credentialsRequest.getUsername(),
                        credentialsRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = userAuthProvider.createToken(authentication);
        String refreshToken = userAuthProvider.createRefreshToken(authentication);

        return new AuthResponse(accessToken, refreshToken);
    }

    public AuthResponse refreshToken(String refreshToken) {
        if (!userAuthProvider.isTokenValid(refreshToken)) {
            throw new UnauthorizedException("Refresh token is invalid");
        }

        String username = userAuthProvider.getUsernameFromToken(refreshToken);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        String newAccessToken = userAuthProvider.createToken(user);

        return new AuthResponse(newAccessToken, refreshToken);
    }

    public AccountDto getAccount() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        AccountDto account = new AccountDto();
        account.setId(user.getId());
        account.setUsername(user.getUsername());
        account.setFirstName(user.getFirstName());
        account.setLastName(user.getLastName());
        account.setActive(user.isActive());
        account.setRoles(
                user.getRoles().stream()
                        .map(role -> role.getName().toString())
                        .collect(Collectors.toSet()));

        return account;
    }
}
