package com.example.equipmentmanagement.repository;

import com.example.equipmentmanagement.model.Equipment;
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
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    @Query("SELECT e.id FROM Equipment e")
    Page<Long> findAllIds(Pageable pageable);

    @EntityGraph(value = "equipment-entity-graph")
    List<Equipment> findAllByIdIn(Set<Long> ids, Sort sort);

    @EntityGraph("equipment-entity-graph")
    Page<Equipment> findAllByUserUsername(String username, Pageable pageable);

    boolean existsByInventoryNumber(String inventoryNumber);

    boolean existsBySerialNumber(String serialNumber);
}
