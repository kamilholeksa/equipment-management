package com.example.equipmentmanagement.controller;

import com.example.equipmentmanagement.dto.equipment.EquipmentHistoryDto;
import com.example.equipmentmanagement.service.EquipmentHistoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/equipment-history")
public class EquipmentHistoryController {

    private final EquipmentHistoryService equipmentHistoryService;


    public EquipmentHistoryController(EquipmentHistoryService equipmentHistoryService) {
        this.equipmentHistoryService = equipmentHistoryService;
    }

    @GetMapping("/equipment/{id}")
    public ResponseEntity<List<EquipmentHistoryDto>> getHistoryByEquipmentId(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.equipmentHistoryService.getHistoryByEquipmentId(id));
    }
}
