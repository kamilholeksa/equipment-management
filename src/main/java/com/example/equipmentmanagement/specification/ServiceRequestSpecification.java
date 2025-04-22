package com.example.equipmentmanagement.specification;

import com.example.equipmentmanagement.dto.servicerequest.ServiceRequestFilter;
import com.example.equipmentmanagement.model.ServiceRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

public class ServiceRequestSpecification {

    private ServiceRequestSpecification() {}

    public static Specification<ServiceRequest> withId(Long id) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("id"), id);
    }

    public static Specification<ServiceRequest> withTitle(String manufacturer) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("manufacturer")), "%" + manufacturer.toLowerCase() + "%");
    }

    public static Specification<ServiceRequest> withStatusIn(Set<String> statuses) {
        return (root, query, criteriaBuilder) ->
                root.get("status").in(statuses);
    }

    public static Specification<ServiceRequest> withUserId(Long userId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("user").get("id"), userId);
    }

    public static Specification<ServiceRequest> withTechnicianId(Long userId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("technician").get("id"), userId);
    }

    public static Specification<ServiceRequest> prepareSpecification(ServiceRequestFilter filter) {
        Specification<ServiceRequest> spec = Specification.where(null);

        if (filter.getId() != null) {
            spec = spec.and(withId(filter.getId()));
        }
        if (filter.getTitle() != null) {
            spec = spec.and(withTitle(filter.getTitle()));
        }
        if (filter.getStatus() != null) {
            spec = spec.and(withStatusIn(filter.getStatus()));
        }
        if (filter.getUserId() != null) {
            spec = spec.and(withUserId(filter.getUserId()));
        }
        if (filter.getTechnicianId() != null) {
            spec = spec.and(withTechnicianId(filter.getUserId()));
        }

        return spec;
    }
}
