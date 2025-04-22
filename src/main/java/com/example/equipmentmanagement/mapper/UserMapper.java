package com.example.equipmentmanagement.mapper;

import com.example.equipmentmanagement.dto.user.UserDto;
import com.example.equipmentmanagement.dto.user.UserSaveDto;
import com.example.equipmentmanagement.enumeration.RoleName;
import com.example.equipmentmanagement.model.Role;
import com.example.equipmentmanagement.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = RoleMapper.class)
public interface UserMapper {
    @Mapping(target = "roles", expression = "java(mapRolesToStrings(user.getRoles()))")
    UserDto userToUserDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "roles", expression = "java(mapStringsToRoles(dto.getRoles()))")
    User userSaveDtoToUser(UserSaveDto dto);

    default Set<String> mapRolesToStrings(Set<Role> roles) {
        if (roles.isEmpty()) return new HashSet<>();
        return roles.stream()
                .map(role ->
                        role.getName().name())
                .collect(Collectors.toSet());
    }

    default Set<Role> mapStringsToRoles(Set<String> roles) {
        if (roles.isEmpty()) return new HashSet<>();
        return roles.stream()
                .map(roleName -> {
                    Role role = new Role();
                    role.setName(RoleName.valueOf(roleName));
                    return role;
                })
                .collect(Collectors.toSet());
    }
}
