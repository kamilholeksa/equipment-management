package com.example.equipmentmanagement.repository;

import com.example.equipmentmanagement.model.Role;
import com.example.equipmentmanagement.enumeration.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

    Optional<Role> findByName(RoleName name);
}
