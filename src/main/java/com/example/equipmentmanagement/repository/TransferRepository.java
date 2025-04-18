package com.example.equipmentmanagement.repository;

import com.example.equipmentmanagement.enumeration.TransferStatus;
import com.example.equipmentmanagement.model.Transfer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {
    @EntityGraph("transfer-entity-graph")
    Page<Transfer> findAll(Pageable pageable);

    @EntityGraph("transfer-entity-graph")
    Page<Transfer> getTransfersByObtainerUsernameAndStatus(String username, TransferStatus status, Pageable pageable);

    @EntityGraph("transfer-entity-graph")
    Page<Transfer> getTransfersByTransferorUsernameOrObtainerUsername(String transferor, String obtainer, Pageable pageable);
}
