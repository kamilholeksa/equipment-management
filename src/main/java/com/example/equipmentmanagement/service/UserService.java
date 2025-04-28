package com.example.equipmentmanagement.service;

import com.example.equipmentmanagement.dto.user.*;
import com.example.equipmentmanagement.enumeration.RoleName;
import com.example.equipmentmanagement.exception.*;
import com.example.equipmentmanagement.mapper.UserMapper;
import com.example.equipmentmanagement.model.Role;
import com.example.equipmentmanagement.model.User;
import com.example.equipmentmanagement.repository.UserRepository;
import com.example.equipmentmanagement.specification.UserSpecification;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder, RoleService roleService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    public Page<UserDto> getAllUsers(UserFilter filter, Pageable pageable) {
        Specification<User> spec = UserSpecification.prepareSpecification(filter);
        return userRepository.findAll(spec, pageable).map(userMapper::userToUserDto);
    }

    public List<UserDto> getActiveUsers() {
        return userRepository.findAllByActiveTrue().stream()
                .map(userMapper::userToUserDto)
                .toList();
    }

    public List<UserDto> getActiveTechniciansUsers() {
        Role technicianRole = roleService.getRoleByName(RoleName.ROLE_TECHNICIAN);

        return userRepository.findAllByActiveTrueAndRolesContaining(technicianRole).stream()
                .map(userMapper::userToUserDto)
                .toList();
    }

    public UserDto getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        return userMapper.userToUserDto(user);
    }

    public User getCurrentUserEntity() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    public UserDto getUser(Long id) {
        return userMapper.userToUserDto(findUserById(id));
    }

    @Transactional
    public UserDto createUser(UserSaveDto dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new BadRequestAlertException(String.format("User with username \"%s\" already exists", dto.getUsername()));
        }

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new BadRequestAlertException(String.format("User with email \"%s\" already exists", dto.getEmail()));
        }

        User newUser = userMapper.userSaveDtoToUser(dto);
        newUser.setPassword(passwordEncoder.encode(dto.getPassword()));

        return userMapper.userToUserDto(userRepository.save(newUser));
    }

    @Transactional
    public UserDto updateUser(Long id, UserSaveDto dto) {
        User existingUser = findUserById(id);

        if (userWithUsernameExists(dto, existingUser)) {
            throw new BadRequestAlertException(String.format("User with username \"%s\" already exists", dto.getUsername()));
        }
        if (userWithEmailExists(dto, existingUser)) {
            throw new BadRequestAlertException(String.format("User with email \"%s\" already exists", dto.getEmail()));
        }

        User updatedUser = userMapper.userSaveDtoToUser(dto);
        updatedUser.setId(existingUser.getId());
        updatedUser.setPassword(existingUser.getPassword());

        return userMapper.userToUserDto(userRepository.save(updatedUser));
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.delete(findUserById(id));
    }

    @Transactional
    public void changePassword(Long id, AdminPasswordChangeDto dto) {
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            throw new PasswordMismatchException();
        }

        User user = findUserById(id);
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void changeCurrentUserPassword(UserPasswordChangeDto dto) {
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            throw new PasswordMismatchException();
        }

        User user = this.getCurrentUserEntity();

        if (!passwordEncoder.matches(String.valueOf(dto.getCurrentPassword()), user.getPassword())) {
            throw new InvalidPasswordException();
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void toggleActive(Long id) {
        User user = findUserById(id);
        user.setActive(!user.isActive());
        userRepository.save(user);
    }

    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
    }

    // Method checks if username already exists and provided username is not matching updated user username.
    private boolean userWithUsernameExists(UserSaveDto dto, User existingUser) {
        return !dto.getUsername().equals(existingUser.getUsername())
                && userRepository.existsByUsername(dto.getUsername());
    }

    // Method checks if email already exists and provided email is not matching updated user email.
    private boolean userWithEmailExists(UserSaveDto dto, User existingUser) {
        return !dto.getEmail().equals(existingUser.getEmail())
                && userRepository.existsByEmail(dto.getEmail());
    }
}
