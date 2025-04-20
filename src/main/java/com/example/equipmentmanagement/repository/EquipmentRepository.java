package com.example.equipmentmanagement.repository;

import com.example.equipmentmanagement.model.Equipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long>, JpaSpecificationExecutor<Equipment> {
    @EntityGraph(value = "equipment-entity-graph")
    Page<Equipment> findAll(Specification<Equipment> spec, Pageable pageable);

    boolean existsByInventoryNumber(String inventoryNumber);

    boolean existsBySerialNumber(String serialNumber);

    int countByAddressId(Long addressId);

    int countByTypeId(Long typeId);
}
