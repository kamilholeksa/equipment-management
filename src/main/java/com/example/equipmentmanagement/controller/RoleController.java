package com.example.equipmentmanagement.controller;

import com.example.equipmentmanagement.dto.RoleDto;
import com.example.equipmentmanagement.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/role")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping()
    public ResponseEntity<List<RoleDto>> getAllRoles() {
        return ResponseEntity.ok(this.roleService.getAllRoles());
    }
}
