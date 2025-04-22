package com.example.equipmentmanagement.mapper;

import com.example.equipmentmanagement.dto.user.RoleDto;
import com.example.equipmentmanagement.model.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleDto roleToRoleDto(Role role);

    Role roleDtoToRole(RoleDto dto);
}
