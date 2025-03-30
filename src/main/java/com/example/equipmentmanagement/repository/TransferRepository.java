package com.example.equipmentmanagement.repository;

import com.example.equipmentmanagement.model.Transfer;
import com.example.equipmentmanagement.model.enumeration.TransferStatus;
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
public interface TransferRepository extends JpaRepository<Transfer, Long> {

    @Query("select t.id from Transfer t")
    Page<Long> findAllIds(Pageable pageable);

    @EntityGraph("transfer-entity-graph")
    List<Transfer> findAllByIdIn(Set<Long> ids, Sort sort);

    @EntityGraph("transfer-entity-graph")
    Page<Transfer> getTransfersByObtainerUsernameAndStatus(String username, TransferStatus status, Pageable pageable);

    @EntityGraph("transfer-entity-graph")
    Page<Transfer> getTransfersByTransferorUsernameOrObtainerUsername(String transferor, String obtainer, Pageable pageable);
}
