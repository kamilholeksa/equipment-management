package com.example.equipmentmanagement.repository;

import com.example.equipmentmanagement.model.Role;
import com.example.equipmentmanagement.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph("user-entity-graph")
    Page<User> findAll(Specification<User> spec, Pageable pageable);

    @EntityGraph("user-entity-graph")
    Page<User> findAll(Pageable pageable);

    @EntityGraph("user-entity-graph")
    List<User> findAllByActiveTrue();

    @EntityGraph("user-entity-graph")
    List<User> findAllByActiveTrueAndRolesContaining(Role role);

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
