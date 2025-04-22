package com.example.equipmentmanagement.service;

import com.example.equipmentmanagement.dto.user.RoleDto;
import com.example.equipmentmanagement.mapper.RoleMapper;
import com.example.equipmentmanagement.exception.ResourceNotFoundException;
import com.example.equipmentmanagement.model.Role;
import com.example.equipmentmanagement.enumeration.RoleName;
import com.example.equipmentmanagement.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleService(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    public List<RoleDto> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(roleMapper::roleToRoleDto)
                .toList();
    }

    public Role getRoleByName(RoleName name) {
        return roleRepository.findByName(name).orElseThrow(
                () -> new ResourceNotFoundException("Role", name.toString())
        );
    }

}
