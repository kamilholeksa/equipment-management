package com.example.equipmentmanagement.repository;

import com.example.equipmentmanagement.model.ServiceRequest;
import com.example.equipmentmanagement.model.enumeration.ServiceRequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, Long> {

    @Query("SELECT sr.id FROM ServiceRequest sr")
    Page<Long> findAllIds(Pageable pageable);

    @EntityGraph("service-request-entity-graph")
    List<ServiceRequest> findAllByIdIn(Set<Long> ids, Sort sort);

    @EntityGraph("service-request-entity-graph")
    Page<ServiceRequest> findAllByStatusIn(Set<ServiceRequestStatus> status, Pageable pageable);

    @EntityGraph("service-request-entity-graph")
    Page<ServiceRequest> findAllByEquipmentId(Long equipmentId, Pageable pageable);
}
