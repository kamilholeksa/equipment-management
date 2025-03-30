package com.example.equipmentmanagement.service;

import com.example.equipmentmanagement.dto.RoleDto;
import com.example.equipmentmanagement.dto.mapper.RoleMapper;
import com.example.equipmentmanagement.exception.ResourceNotFoundException;
import com.example.equipmentmanagement.model.Role;
import com.example.equipmentmanagement.model.enumeration.RoleName;
import com.example.equipmentmanagement.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<RoleDto> getAllRoles() {
        return this.roleRepository.findAll().stream()
                .map(RoleMapper::toDto)
                .toList();
    }

    public Role getRoleByName(RoleName name) {
        return this.roleRepository.findByName(name).orElseThrow(
                () -> new ResourceNotFoundException("Role", name.toString())
        );
    }

}
