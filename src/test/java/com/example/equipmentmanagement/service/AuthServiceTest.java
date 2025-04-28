package com.example.equipmentmanagement.service;

import com.example.equipmentmanagement.dto.auth.AccountDto;
import com.example.equipmentmanagement.dto.auth.AuthResponse;
import com.example.equipmentmanagement.dto.auth.CredentialsRequest;
import com.example.equipmentmanagement.enumeration.RoleName;
import com.example.equipmentmanagement.exception.InvalidPasswordException;
import com.example.equipmentmanagement.exception.UnauthorizedException;
import com.example.equipmentmanagement.exception.UserNotFoundException;
import com.example.equipmentmanagement.model.Role;
import com.example.equipmentmanagement.model.User;
import com.example.equipmentmanagement.repository.UserRepository;
import com.example.equipmentmanagement.security.jwt.TokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    AuthService authService;

    @Mock
    UserRepository userRepository;
    @Mock
    AuthenticationManager authenticationManager;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    TokenProvider tokenProvider;

    @Test
    @DisplayName("UserNotFoundException thrown when user does not exist")
    void testLogin_whenUserNotFound_throwsUserNotFoundException() {
        // Given
        CredentialsRequest credentialsRequest = new CredentialsRequest();
        credentialsRequest.setUsername("username");
        credentialsRequest.setPassword("password");

        when(userRepository.findByUsername("username")).thenReturn(Optional.empty());

        // When & Then
        UserNotFoundException thrown = assertThrows(UserNotFoundException.class, () -> authService.login(credentialsRequest));
        assertEquals("User username not found", thrown.getMessage());
    }

    @Test
    @DisplayName("UserNotFoundException thrown when user is inactive")
    void testLogin_whenUserInactive_throwsUserNotFoundException() {
        // Given
        CredentialsRequest credentialsRequest = new CredentialsRequest();
        credentialsRequest.setUsername("username");
        credentialsRequest.setPassword("password");

        User user = new User();
        user.setUsername("username");
        user.setPassword("passwordHash");
        user.setActive(false);

        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));

        // When & Then
        UserNotFoundException thrown = assertThrows(UserNotFoundException.class, () -> authService.login(credentialsRequest));
        assertEquals("User username not found", thrown.getMessage());
    }

    @Test
    @DisplayName("InvalidPasswordException thrown when wrong password provided")
    void testLogin_whenPasswordInvalid_throwsInvalidPasswordException() {
        // Given
        CredentialsRequest credentialsRequest = new CredentialsRequest();
        credentialsRequest.setUsername("username");
        credentialsRequest.setPassword("password");

        User user = new User();
        user.setUsername("username");
        user.setPassword("passwordHash");
        user.setActive(true);

        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(CharSequence.class), anyString())).thenReturn(false);

        // When & Then
        InvalidPasswordException thrown = assertThrows(InvalidPasswordException.class, () -> authService.login(credentialsRequest));
        assertEquals("Invalid password", thrown.getMessage());
    }

    @Test
    @DisplayName("AuthResponse returned when valid credentials provided")
    void testLogin_whenValidCredentialsProvided_returnsAuthResponse() {
        // Given
        User user = new User();
        user.setId(1L);
        user.setUsername("username");
        user.setPassword("passwordHash");
        user.setActive(true);

        Authentication authentication = mock(Authentication.class);

        CredentialsRequest credentialsRequest = new CredentialsRequest();
        credentialsRequest.setUsername("username");
        credentialsRequest.setPassword("password");

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(CharSequence.class), anyString())).thenReturn(true);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(tokenProvider.createToken(any(Authentication.class))).thenReturn("accessToken");
        when(tokenProvider.createRefreshToken(any(Authentication.class))).thenReturn("refreshToken");

        // When
        AuthResponse result = authService.login(credentialsRequest);

        //Then
        assertEquals("accessToken", result.getAccessToken());
        assertEquals("refreshToken", result.getRefreshToken());
        verify(tokenProvider).createToken(any(Authentication.class));
        verify(tokenProvider).createRefreshToken(any(Authentication.class));
    }

    @Test
    @DisplayName("UnauthorizedException thrown when wrong refresh token is invalid")
    void testRefreshToken_whenRefreshTokenIsInvalid_throwsUnauthorizedException() {
        // Given
        String expectedMessage = "Refresh token is invalid";
        when(tokenProvider.isTokenValid(anyString())).thenReturn(false);

        // When & Then
        UnauthorizedException thrown = assertThrows(UnauthorizedException.class, () -> authService.refreshToken("refreshToken"));
        assertEquals(expectedMessage, thrown.getMessage());
        verify(tokenProvider, never()).createToken(any(User.class));
    }

    @Test
    @DisplayName("UserNotFoundException thrown when user does not exist")
    void testRefreshToken_whenUserNotFound_throwsUserNotFoundException() {
        // Given
        String username = "username";
        String expectedMessage = "User username not found";

        when(tokenProvider.isTokenValid(anyString())).thenReturn(true);
        when(tokenProvider.getUsernameFromToken(anyString())).thenReturn(username);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        // When & Then
        UserNotFoundException thrown = assertThrows(UserNotFoundException.class, () -> authService.refreshToken("refreshToken"));
        assertEquals(expectedMessage, thrown.getMessage());
        verify(tokenProvider, never()).createToken(any(User.class));
    }

    @Test
    @DisplayName("UserNotFoundException thrown when user does not exist")
    void testRefreshToken_whenUserIsInactive_throwsUserNotFoundException() {
        // Given
        String username = "username";
        User user = new User();
        user.setActive(false);
        String expectedMessage = "User username not found";

        when(tokenProvider.isTokenValid(anyString())).thenReturn(true);
        when(tokenProvider.getUsernameFromToken(anyString())).thenReturn(username);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        // When & Then
        UserNotFoundException thrown = assertThrows(UserNotFoundException.class, () -> authService.refreshToken("refreshToken"));
        assertEquals(expectedMessage, thrown.getMessage());
        verify(tokenProvider, never()).createToken(any(User.class));
    }

    @Test
    @DisplayName("UserNotFoundException thrown when user does not exist")
    void testRefreshToken_whenTokenValidAndUserActive_returnsAuthResponse() {
        // Given
        String username = "username";
        User user = new User();
        user.setActive(true);

        when(tokenProvider.isTokenValid(anyString())).thenReturn(true);
        when(tokenProvider.getUsernameFromToken(anyString())).thenReturn(username);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(tokenProvider.createToken(any(User.class))).thenReturn("accessToken");

        // When
        AuthResponse result = authService.refreshToken("refreshToken");

        // Then
        assertEquals("accessToken", result.getAccessToken());
        verify(tokenProvider, never()).createRefreshToken(any(Authentication.class));
    }

    @Test
    @DisplayName("UserNotFoundException thrown when user does not exist")
    void testGetAccount_whenUserNotFound_throwsUserNotFoundException() {
        // Given
        String expectedMessage = "User username not found";
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.setContext(new SecurityContextImpl(authentication));

        when(authentication.getName()).thenReturn("username");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        // When & Then
        UserNotFoundException thrown = assertThrows(UserNotFoundException.class, () -> authService.getAccount());
        assertEquals(expectedMessage, thrown.getMessage());
    }

    @Test
    @DisplayName("AccountDto returned when user found")
    void testGetAccount_whenUserFound_returnsAccountDto() {
        // Given
        User user = new User();
        user.setId(1L);
        user.setUsername("username");
        user.setFirstName("Firstname");
        user.setLastName("Lastname");
        user.setActive(true);
        Role role = new Role();
        role.setName(RoleName.ROLE_USER);
        user.setRoles(Set.of(role));

        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.setContext(new SecurityContextImpl(authentication));

        when(authentication.getName()).thenReturn("username");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        // When
        AccountDto result = authService.getAccount();

        // Then
        assertEquals(1L, result.getId());
        assertEquals("username", result.getUsername());
        assertEquals("Firstname", result.getFirstName());
        assertEquals("Lastname", result.getLastName());
        assertTrue(result.isActive());
        assertTrue(result.getRoles().contains("ROLE_USER"));
    }
}