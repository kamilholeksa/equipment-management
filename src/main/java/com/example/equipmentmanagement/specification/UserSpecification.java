package com.example.equipmentmanagement.specification;

import com.example.equipmentmanagement.dto.user.UserFilter;
import com.example.equipmentmanagement.model.User;
import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

public class UserSpecification {

    private UserSpecification() {}

    public static Specification<User> withId(Long id) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("id"), id);
    }

    public static Specification<User> withFirstName(String firstName) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), "%" + firstName.toLowerCase() + "%");
    }

    public static Specification<User> withLastName(String lastName) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), "%" + lastName.toLowerCase() + "%");
    }

    public static Specification<User> withUsername(String username) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), "%" + username.toLowerCase() + "%");
    }

    public static Specification<User> withEmail(String email) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + email.toLowerCase() + "%");
    }

    public static Specification<User> withPhoneNumber(String phoneNumber) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("phoneNumber")), "%" + phoneNumber.toLowerCase() + "%");
    }

    public static Specification<User> withRolesIn(Set<String> roles) {
        return (root, query, criteriaBuilder) ->
                root.join("roles").get("name").in(roles);
    }

    public static Specification<User> withActive(Boolean active) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("active"), active);
    }

    public static Specification<User> prepareSpecification(UserFilter filter) {
        Specification<User> spec = Specification.where(null);

        if (filter.getId() != null) {
            spec = spec.and(withId(filter.getId()));
        }
        if (filter.getFirstName() != null) {
            spec = spec.and(withFirstName(filter.getFirstName()));
        }
        if (filter.getLastName() != null) {
            spec = spec.and(withLastName(filter.getLastName()));
        }
        if (filter.getUsername() != null) {
            spec = spec.and(withUsername(filter.getUsername()));
        }
        if (filter.getEmail() != null) {
            spec = spec.and(withEmail(filter.getEmail()));
        }
        if (filter.getPhoneNumber() != null) {
            spec = spec.and(withPhoneNumber(filter.getPhoneNumber()));
        }
        if (filter.getRoles() != null) {
            spec = spec.and(withRolesIn(filter.getRoles()));
        }
        if (filter.getActive() != null) {
            spec = spec.and(withActive(filter.getActive()));
        }

        return spec;
    }
}
