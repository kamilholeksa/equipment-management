package com.example.equipmentmanagement.controller;

import com.example.equipmentmanagement.dto.user.AdminPasswordChangeDto;
import com.example.equipmentmanagement.dto.user.UserDto;
import com.example.equipmentmanagement.dto.user.UserPasswordChangeDto;
import com.example.equipmentmanagement.dto.user.UserSaveDto;
import com.example.equipmentmanagement.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<Page<UserDto>> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok(this.userService.getAllUsers(pageable));
    }

    @GetMapping("/active")
    public ResponseEntity<List<UserDto>> getActiveUsers() {
        return ResponseEntity.ok(this.userService.getActiveUsers());
    }

    @GetMapping("/active-technicians")
    public ResponseEntity<List<UserDto>> getActiveTechniciansUsers() {
        return ResponseEntity.ok(this.userService.getActiveTechniciansUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable("id") Long id) {
        return ResponseEntity.ok(this.userService.getUser(id));
    }

    @GetMapping("/account")
    public ResponseEntity<UserDto> getCurrentUser() {
        return ResponseEntity.ok(this.userService.getCurrentUser());
    }

    @PostMapping()
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserSaveDto dto) {
        UserDto result = userService.createUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("id") Long id, @Valid @RequestBody UserSaveDto dto) {
        UserDto result = this.userService.updateUser(id, dto);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PatchMapping("/{id}/change-password")
    public ResponseEntity<ResponseMessage> changePassword(@PathVariable("id") Long id, @Valid @RequestBody AdminPasswordChangeDto dto) {
        this.userService.changePassword(id, dto);
        return ResponseEntity.ok(new ResponseMessage("Password changed successfully"));
    }

    @PatchMapping("/change-password")
    public ResponseEntity<ResponseMessage> changeCurrentUserPassword(@Valid @RequestBody UserPasswordChangeDto dto) {
        this.userService.changeCurrentUserPassword(dto);
        return ResponseEntity.ok(new ResponseMessage("Password changed successfully"));
    }

    @PatchMapping("{id}/toggle-active")
    public ResponseEntity<ResponseMessage> toggleActive(@PathVariable("id") Long id) {
        this.userService.toggleActive(id);
        return ResponseEntity.ok(new ResponseMessage("User status changed successfully"));
    }
}
