package com.example.equipmentmanagement.repository;

import com.example.equipmentmanagement.model.EquipmentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentHistoryRepository extends JpaRepository<EquipmentHistory, Long> {
    List<EquipmentHistory> findByEquipmentId(Long equipmentId);
}
