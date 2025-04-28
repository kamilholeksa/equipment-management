package com.example.equipmentmanagement.service;

import com.example.equipmentmanagement.dto.user.*;
import com.example.equipmentmanagement.enumeration.RoleName;
import com.example.equipmentmanagement.exception.*;
import com.example.equipmentmanagement.mapper.UserMapper;
import com.example.equipmentmanagement.model.Role;
import com.example.equipmentmanagement.model.User;
import com.example.equipmentmanagement.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;
    @Mock
    UserMapper userMapper;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    RoleService roleService;

    private UserSaveDto userSaveDto;

    @BeforeEach
    void setUp() {
        userSaveDto = new UserSaveDto();
        userSaveDto.setFirstName("Firstname");
        userSaveDto.setLastName("Lastname");
        userSaveDto.setUsername("username");
        userSaveDto.setPassword("password");
        userSaveDto.setEmail("email@test.com");
        userSaveDto.setPhoneNumber("123456789");
        userSaveDto.setActive(true);
        userSaveDto.setRoles(Set.of("ROLE_USER"));
    }

    @Test
    @DisplayName("Page of UserDto objects returned")
    void testGetAllUsers_returnsPageOfUserDto() {
        // Given
        User user = new User();
        UserDto dto = new UserDto();
        Page<User> page = new PageImpl<>(List.of(user));
        UserFilter filter = new UserFilter();
        PageRequest pageRequest = PageRequest.of(0, 10);

        when(userRepository.findAll(ArgumentMatchers.<Specification<User>>any(), any(PageRequest.class))).thenReturn(page);
        when(userMapper.userToUserDto(any(User.class))).thenReturn(dto);

        // When
        Page<UserDto> result = userService.getAllUsers(filter, pageRequest);

        // Then
        assertEquals(1, result.getTotalElements());
        assertEquals(dto, result.getContent().getFirst());
        verify(userRepository).findAll(ArgumentMatchers.<Specification<User>>any(), any(PageRequest.class));
    }

    @Test
    @DisplayName("List of UserDto returned")
    void testGetActiveUsers_returnsListOfUserDto() {
        // Given
        User user = new User();
        UserDto dto = new UserDto();

        when(userRepository.findAllByActiveTrue()).thenReturn(List.of(user));
        when(userMapper.userToUserDto(any(User.class))).thenReturn(dto);

        // When
        List<UserDto> result = userService.getActiveUsers();

        // Then
        assertEquals(1, result.size());
        assertEquals(dto, result.getFirst());
        verify(userRepository).findAllByActiveTrue();
    }

    @Test
    @DisplayName("List of UserDto returned")
    void testGetActiveTechniciansUsers_returnsListOfUserDtoWithRoleTechnician() {
        // Given
        User user = new User();
        UserDto dto = new UserDto();
        Role technicianRole = new Role();
        technicianRole.setName(RoleName.ROLE_TECHNICIAN);

        when(roleService.getRoleByName(any(RoleName.class))).thenReturn(technicianRole);
        when(userRepository.findAllByActiveTrueAndRolesContaining(any(Role.class))).thenReturn(List.of(user));
        when(userMapper.userToUserDto(any(User.class))).thenReturn(dto);

        // When
        List<UserDto> result = userService.getActiveTechniciansUsers();

        // Then
        assertEquals(1, result.size());
        assertEquals(dto, result.getFirst());
        verify(userRepository).findAllByActiveTrueAndRolesContaining(any(Role.class));
    }

    @Test
    @DisplayName("UserNotFoundException thrown when user does not exist")
    void testGetCurrentUser_whenUserNotFound_throwsUserNotFoundException() {
        // Given
        String expectedMessage = "User username not found";
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.setContext(new SecurityContextImpl(authentication));

        when(authentication.getName()).thenReturn("username");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        // When & Then
        UserNotFoundException thrown = assertThrows(UserNotFoundException.class, () -> userService.getCurrentUser());
        assertEquals(expectedMessage, thrown.getMessage());
    }

    @Test
    @DisplayName("UserDto of current user returned")
    void testGetCurrentUser_whenUserFound_returnsUserDto() {
        // Given
        User user = new User();
        UserDto dto = new UserDto();

        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.setContext(new SecurityContextImpl(authentication));

        when(authentication.getName()).thenReturn("username");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(userMapper.userToUserDto(any(User.class))).thenReturn(dto);

        // When
        UserDto result = userService.getCurrentUser();

        // Then
        assertEquals(dto, result);
        verify(userRepository).findByUsername(anyString());
    }

    @Test
    @DisplayName("UserNotFoundException thrown when user does not exist")
    void testGetCurrentUserEntity_whenUserNotFound_throwsUserNotFoundException() {
        // Given
        String expectedMessage = "User username not found";
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.setContext(new SecurityContextImpl(authentication));

        when(authentication.getName()).thenReturn("username");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        // When & Then
        UserNotFoundException thrown = assertThrows(UserNotFoundException.class, () -> userService.getCurrentUserEntity());
        assertEquals(expectedMessage, thrown.getMessage());
    }

    @Test
    @DisplayName("Current user object returned")
    void testGetCurrentUserEntity_whenUserFound_returnsUser() {
        // Given
        User user = new User();

        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.setContext(new SecurityContextImpl(authentication));

        when(authentication.getName()).thenReturn("username");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        // When
        User result = userService.getCurrentUserEntity();

        // Then
        assertEquals(user, result);
        verify(userRepository).findByUsername(anyString());
    }

    @Test
    @DisplayName("UserNotFoundException thrown when user does not exist")
    void testGetUser_whenUserNotFound_throwsResourceNotFoundException() {
        // Given
        String expectedMessage = "User with id 1 not found";

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> userService.getUser(1L));
        assertEquals(expectedMessage, thrown.getMessage());
    }

    @Test
    @DisplayName("UserDto returned")
    void testGetUser_whenUserFound_returnsUserDto() {
        // Given
        User user = new User();
        UserDto dto = new UserDto();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userMapper.userToUserDto(any(User.class))).thenReturn(dto);

        // When
        UserDto result = userService.getUser(1L);

        // Then
        assertEquals(dto, result);
        verify(userRepository).findById(anyLong());
    }

    @Test
    @DisplayName("BadRequestAlertException thrown when username already exists")
    void testCreateUser_whenUserWithUsernameExists_throwsBadRequestAlertException() {
        // Given
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        // When & Then
        BadRequestAlertException thrown = assertThrows(BadRequestAlertException.class, () -> userService.createUser(userSaveDto));
        assertEquals("User with username \"username\" already exists", thrown.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("BadRequestAlertException thrown when email already exists")
    void testCreateUser_whenUserWithEmailExists_throwsBadRequestAlertException() {
        // Given
        String expectedMessage = "User with email \"email@test.com\" already exists";
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        // When & Then
        BadRequestAlertException thrown = assertThrows(BadRequestAlertException.class, () -> userService.createUser(userSaveDto));
        assertEquals(expectedMessage, thrown.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("User created and UserDto returned")
    void testCreateUser_whenUserValid_returnsUserDto() {
        // Given
        User user = new ModelMapper().map(userSaveDto, User.class);
        user.setPassword("passwordHash");
        user.setId(1L);
        UserDto dto = new ModelMapper().map(userSaveDto, UserDto.class);
        dto.setId(1L);
        dto.setCreatedBy("createdBy");
        dto.setCreatedDate(Instant.parse("2025-04-28T10:15:30.00Z"));

        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userMapper.userSaveDtoToUser(any(UserSaveDto.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.userToUserDto(any(User.class))).thenReturn(dto);

        // When
        UserDto result = userService.createUser(userSaveDto);

        // Then
        assertNotNull(result.getId());
        assertEquals(userSaveDto.getFirstName(), result.getFirstName());
        assertEquals(userSaveDto.getLastName(), result.getLastName());
        assertEquals(userSaveDto.getUsername(), result.getUsername());
        assertEquals(userSaveDto.getEmail(), result.getEmail());
        assertEquals(userSaveDto.getPhoneNumber(), result.getPhoneNumber());
        assertTrue(result.isActive());
        assertEquals(userSaveDto.getRoles(), result.getRoles());
        assertNotNull(result.getCreatedBy());
        assertNotNull(result.getCreatedDate());
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("UserNotFoundException thrown when user does not exist")
    void testUpdateUser_whenUserNotFound_throwsResourceNotFoundException() {
        // Given
        String expectedMessage = "User with id 1 not found";
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(1L, userSaveDto));
        assertEquals(expectedMessage, thrown.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("BadRequestAlertException thrown when another user with provided username already exists")
    void testUpdateUser_whenUsernameAlreadyExists_throwsBadRequestAlertException() {
        // Given
        User user = new User();
        String expectedMessage = "User with username \"username\" already exists";
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        // When & Then
        BadRequestAlertException thrown = assertThrows(BadRequestAlertException.class, () -> userService.updateUser(1L, userSaveDto));
        assertEquals(expectedMessage, thrown.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("BadRequestAlertException thrown when another user with provided email already exists")
    void testUpdateUser_whenEmailAlreadyExists_throwsBadRequestAlertException() {
        // Given
        User user = new User();
        String expectedMessage = "User with email \"email@test.com\" already exists";
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        // When & Then
        BadRequestAlertException thrown = assertThrows(BadRequestAlertException.class, () -> userService.updateUser(1L, userSaveDto));
        assertEquals(expectedMessage, thrown.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("User updated and UserDto returned")
    void testUpdateUser_whenUserValid_returnsUserDto() {
        // Given
        User existingUser = new ModelMapper().map(userSaveDto, User.class);
        existingUser.setId(1L);
        existingUser.setFirstName("Firstname2");
        existingUser.setLastName("Lastname2");

        User user = new ModelMapper().map(userSaveDto, User.class);
        user.setPassword("passwordHash");
        user.setId(1L);

        UserDto dto = new ModelMapper().map(userSaveDto, UserDto.class);
        dto.setId(1L);
        dto.setLastModifiedBy("createdBy");
        dto.setLastModifiedDate(Instant.parse("2025-04-28T10:15:30.00Z"));

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(existingUser));
        when(userMapper.userSaveDtoToUser(any(UserSaveDto.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.userToUserDto(any(User.class))).thenReturn(dto);

        // When
        UserDto result = userService.updateUser(1L, userSaveDto);

        // Then
        assertNotNull(result.getId());
        assertEquals(userSaveDto.getFirstName(), result.getFirstName());
        assertEquals(userSaveDto.getLastName(), result.getLastName());
        assertEquals(userSaveDto.getUsername(), result.getUsername());
        assertEquals(userSaveDto.getEmail(), result.getEmail());
        assertEquals(userSaveDto.getPhoneNumber(), result.getPhoneNumber());
        assertTrue(result.isActive());
        assertEquals(userSaveDto.getRoles(), result.getRoles());
        assertNotNull(result.getLastModifiedBy());
        assertNotNull(result.getLastModifiedDate());
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("ResourceNotFoundException thrown when user does not exist")
    void testDeleteUser_whenUserNotFound_throwsResourceNotFoundException() {
        // Given
        String expectedMessage = "User with id 1 not found";
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(1L));
        assertEquals(expectedMessage, thrown.getMessage());
        verify(userRepository, never()).delete(any(User.class));
    }

    @Test
    @DisplayName("User deleted")
    void testDeleteUser_deletesUser() {
        // Given
        User user = new User();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        // When
        userService.deleteUser(1L);

        // Then
        verify(userRepository).delete(any(User.class));
    }

    @Test
    @DisplayName("PasswordMismatchException thrown when passwords do not match")
    void testChangePassword_whenPasswordsDoNotMatch_throwsPasswordMismatchException() {
        // Given
        AdminPasswordChangeDto passwordChangeDto = new AdminPasswordChangeDto();
        passwordChangeDto.setNewPassword("password");
        passwordChangeDto.setConfirmPassword("password1");
        String expectedMessage = "Password confirmation does not match new password";

        // When & Then
        PasswordMismatchException thrown = assertThrows(PasswordMismatchException.class, () -> userService.changePassword(1L, passwordChangeDto));
        assertEquals(expectedMessage, thrown.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("ResourceNotFoundException thrown when user does not exist")
    void testChangePassword_whenUserNotFound_throwsResourceNotFoundException() {
        // Given
        AdminPasswordChangeDto passwordChangeDto = new AdminPasswordChangeDto();
        passwordChangeDto.setNewPassword("password");
        passwordChangeDto.setConfirmPassword("password");
        String expectedMessage = "User with id 1 not found";

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> userService.changePassword(1L, passwordChangeDto));
        assertEquals(expectedMessage, thrown.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("User password has been changed")
    void testChangePassword_whenValidPasswordsProvided_savesUserWithNewPassword() {
        // Given
        User user = new User();
        AdminPasswordChangeDto passwordChangeDto = new AdminPasswordChangeDto();
        passwordChangeDto.setNewPassword("password");
        passwordChangeDto.setConfirmPassword("password");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(anyString())).thenReturn("passwordHash");

        // When
        userService.changePassword(1L, passwordChangeDto);

        // Then
        assertEquals("passwordHash", user.getPassword());
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("PasswordMismatchException thrown when passwords do not match")
    void testChangeCurrentUserPassword_whenPasswordsDoNotMatch_throwsPasswordMismatchException() {
        // Given
        UserPasswordChangeDto passwordChangeDto = new UserPasswordChangeDto();
        passwordChangeDto.setCurrentPassword("oldPassword");
        passwordChangeDto.setNewPassword("newPassword");
        passwordChangeDto.setConfirmPassword("newPassword1");
        String expectedMessage = "Password confirmation does not match new password";

        // When & Then
        PasswordMismatchException thrown = assertThrows(PasswordMismatchException.class, () -> userService.changeCurrentUserPassword(passwordChangeDto));
        assertEquals(expectedMessage, thrown.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("UserNotFoundException thrown when user does not exist")
    void testChangeCurrentUserPassword_whenUserNotFound_throwsUserNotFoundException() {
        // Given
        UserPasswordChangeDto passwordChangeDto = new UserPasswordChangeDto();
        passwordChangeDto.setCurrentPassword("oldPassword");
        passwordChangeDto.setNewPassword("password");
        passwordChangeDto.setConfirmPassword("password");
        String expectedMessage = "User username not found";

        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.setContext(new SecurityContextImpl(authentication));

        when(authentication.getName()).thenReturn("username");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        // When & Then
        UserNotFoundException thrown = assertThrows(UserNotFoundException.class, () -> userService.changeCurrentUserPassword(passwordChangeDto));
        assertEquals(expectedMessage, thrown.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("InvalidPasswordException thrown when provided password does not match old password")
    void testChangeCurrentUserPassword_whenOldPasswordDoesNotMatch_throwsInvalidPasswordException() {
        // Given
        User user = new User();
        UserPasswordChangeDto passwordChangeDto = new UserPasswordChangeDto();
        passwordChangeDto.setCurrentPassword("oldPassword");
        passwordChangeDto.setNewPassword("password");
        passwordChangeDto.setConfirmPassword("password");
        String expectedMessage = "Invalid password";

        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.setContext(new SecurityContextImpl(authentication));

        when(authentication.getName()).thenReturn("username");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("oldPassword", user.getPassword())).thenReturn(false);

        // When & Then
        InvalidPasswordException thrown = assertThrows(InvalidPasswordException.class, () -> userService.changeCurrentUserPassword(passwordChangeDto));
        assertEquals(expectedMessage, thrown.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("User password has been changed")
    void testChangeCurrentUserPassword_whenValidPasswordsProvided_savesUserWithNewPassword() {
        // Given
        User user = new User();
        UserPasswordChangeDto passwordChangeDto = new UserPasswordChangeDto();
        passwordChangeDto.setCurrentPassword("oldPassword");
        passwordChangeDto.setNewPassword("password");
        passwordChangeDto.setConfirmPassword("password");

        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.setContext(new SecurityContextImpl(authentication));

        when(authentication.getName()).thenReturn("username");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("oldPassword", user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(anyString())).thenReturn("passwordHash");

        // When
        userService.changeCurrentUserPassword(passwordChangeDto);

        // Then
        assertEquals("passwordHash", user.getPassword());
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("Repository save method executed for current user entity")
    void testToggleActive() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> userService.toggleActive(1L));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("User's active field changed and user has been saved")
    void testToggleActive_shouldToggleAndSaveUser() {
        // Given
        User user = new User();
        user.setActive(true);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        // When
        userService.toggleActive(1L);

        // Then
        assertFalse(user.isActive(), "User should now be inactive");
        verify(userRepository).save(user);
    }
}
