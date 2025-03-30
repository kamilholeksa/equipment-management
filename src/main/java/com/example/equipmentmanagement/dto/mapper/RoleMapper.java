package com.example.equipmentmanagement.dto.mapper;

import com.example.equipmentmanagement.dto.RoleDto;
import com.example.equipmentmanagement.model.Role;
import com.example.equipmentmanagement.model.enumeration.RoleName;

public class RoleMapper {

    private RoleMapper() {
    }

    public static RoleDto toDto(Role role) {
        RoleDto dto = new RoleDto();
        dto.setName(role.getName().toString());
        dto.setDescription(role.getDescription());

        return dto;
    }

    public static Role toEntity(String roleName) {
        Role role = new Role();
        role.setName(RoleName.valueOf(roleName));

        return role;
    }
}
