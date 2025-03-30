package com.example.equipmentmanagement.repository;

import com.example.equipmentmanagement.model.ServiceRequestNote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRequestNoteRepository extends JpaRepository<ServiceRequestNote, Long> {
    Page<ServiceRequestNote> findByServiceRequestId(Long serviceRequestId, Pageable pageable);
}
