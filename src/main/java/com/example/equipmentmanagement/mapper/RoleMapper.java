package com.example.equipmentmanagement.mapper;

import com.example.equipmentmanagement.dto.user.RoleDto;
import com.example.equipmentmanagement.model.Role;
import com.example.equipmentmanagement.enumeration.RoleName;

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
