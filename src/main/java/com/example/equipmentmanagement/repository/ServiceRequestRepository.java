package com.example.equipmentmanagement.repository;

import com.example.equipmentmanagement.enumeration.ServiceRequestStatus;
import com.example.equipmentmanagement.model.ServiceRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, Long>, JpaSpecificationExecutor<ServiceRequest> {
    @EntityGraph("service-request-entity-graph")
    Page<ServiceRequest> findAll(Specification<ServiceRequest> spec, Pageable pageable);

    @EntityGraph("service-request-entity-graph")
    Page<ServiceRequest> findAll(Pageable pageable);

    @EntityGraph("service-request-entity-graph")
    Page<ServiceRequest> findAllByStatusIn(Set<ServiceRequestStatus> status, Pageable pageable);

    @EntityGraph("service-request-entity-graph")
    Page<ServiceRequest> findAllByEquipmentId(Long equipmentId, Pageable pageable);
}
