package com.example.equipmentmanagement.service;

import com.example.equipmentmanagement.dto.*;
import com.example.equipmentmanagement.dto.mapper.RoleMapper;
import com.example.equipmentmanagement.dto.mapper.UserMapper;
import com.example.equipmentmanagement.exception.InvalidPasswordException;
import com.example.equipmentmanagement.exception.PasswordMismatchException;
import com.example.equipmentmanagement.exception.ResourceNotFoundException;
import com.example.equipmentmanagement.exception.UserNotFoundException;
import com.example.equipmentmanagement.model.Role;
import com.example.equipmentmanagement.model.User;
import com.example.equipmentmanagement.model.enumeration.RoleName;
import com.example.equipmentmanagement.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.equipmentmanagement.dto.mapper.UserMapper.toDto;
import static com.example.equipmentmanagement.dto.mapper.UserMapper.toEntity;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleService roleService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    public Page<UserDto> getAllUsers(
            int pageNumber,
            int pageSize,
            String sortField,
            String sortOrder
    ) {
        Sort sort = Sort.by(sortOrder.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortField);
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        Page<Long> idsPage = userRepository.findAllIds(pageRequest);
        Set<Long> ids = Set.copyOf(idsPage.getContent());

        List<UserDto> users = userRepository.findAllByIdIn(ids, idsPage.getSort()).stream()
                .map(UserMapper::toDto)
                .toList();

        return new PageImpl<>(users, pageRequest, idsPage.getTotalElements());
    }

    public List<UserDto> getActiveUsers() {
        return this.userRepository.findAllByActiveTrue().stream()
                .map(UserMapper::toDto)
                .toList();
    }

    public List<UserDto> getActiveTechniciansUsers() {
        Role technicianRole = roleService.getRoleByName(RoleName.ROLE_TECHNICIAN);

        return this.userRepository.findAllByActiveTrueAndRolesContaining(technicianRole).stream()
                .map(UserMapper::toDto)
                .toList();
    }

    public UserDto getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        return toDto(user);
    }

    public User getCurrentUserEntity() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    public UserDto getUser(Long id) {
        return toDto(findUserById(id));
    }

    @Transactional
    public UserDto createUser(UserSaveDto dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new ValidationException(String.format("User with username \"%s\" already exists", dto.getUsername()));
        }

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ValidationException(String.format("User with email \"%s\" already exists", dto.getEmail()));
        }

        Set<Role> roles = dto.getRoles().stream()
                .map(RoleMapper::toEntity)
                .collect(Collectors.toSet());

        User newUser = toEntity(dto, roles);
        newUser.setPassword(passwordEncoder.encode(dto.getPassword()));

        return toDto(userRepository.save(newUser));
    }

    @Transactional
    public UserDto updateUser(Long id, UserSaveDto dto) {
        User existingUser = findUserById(id);

        if ((!dto.getUsername().equals(existingUser.getUsername())) && userRepository.existsByUsername(dto.getUsername())) {
            throw new ValidationException(String.format("User with username \"%s\" already exists", dto.getUsername()));
        }

        if ((!dto.getEmail().equals(existingUser.getEmail())) && userRepository.existsByEmail(dto.getEmail())) {
            throw new ValidationException(String.format("User with username \"%s\" already exists", dto.getEmail()));
        }

        Set<Role> roles = dto.getRoles().stream()
                .map(RoleMapper::toEntity)
                .collect(Collectors.toSet());

        User updatedUser = toEntity(dto, roles);
        updatedUser.setId(existingUser.getId());
        updatedUser.setPassword(existingUser.getPassword());

        return toDto(userRepository.save(updatedUser));
    }

    @Transactional
    public void changePassword(Long id, AdminPasswordChangeDto dto) {
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            throw new PasswordMismatchException();
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));

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
}
