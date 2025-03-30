package com.example.equipmentmanagement.service;

import com.example.equipmentmanagement.dto.CredentialsDto;
import com.example.equipmentmanagement.dto.UserAuthDto;
import com.example.equipmentmanagement.exception.InvalidPasswordException;
import com.example.equipmentmanagement.exception.UserNotFoundException;
import com.example.equipmentmanagement.model.User;
import com.example.equipmentmanagement.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserAuthDto login(CredentialsDto credentialsDto) {
        User user = userRepository.findByUsername(credentialsDto.getUsername())
                .orElseThrow(() -> new UserNotFoundException(credentialsDto.getUsername()));

        if (!user.isActive()) {
            throw new UserNotFoundException(user.getUsername());
        }

        if (passwordEncoder.matches(CharBuffer.wrap(credentialsDto.getPassword()), user.getPassword())) {
            UserAuthDto userAuthDto = new UserAuthDto();
            userAuthDto.setFirstName(user.getFirstName());
            userAuthDto.setLastName(user.getLastName());
            userAuthDto.setUsername(user.getUsername());
            userAuthDto.setEmail(user.getEmail());
            userAuthDto.setRoles(user.getRoles().stream()
                    .map(role -> role.getName().toString())
                    .toList());

            return userAuthDto;
        } else {
            throw new InvalidPasswordException();
        }

    }
}
