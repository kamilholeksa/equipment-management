package com.example.equipmentmanagement.controller;

import com.example.equipmentmanagement.dto.equipment.EquipmentTypeDto;
import com.example.equipmentmanagement.dto.equipment.EquipmentTypeSaveDto;
import com.example.equipmentmanagement.service.EquipmentTypeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipment-type")
public class EquipmentTypeController {

    private final EquipmentTypeService equipmentTypeService;


    public EquipmentTypeController(EquipmentTypeService equipmentTypeService) {
        this.equipmentTypeService = equipmentTypeService;
    }

    @GetMapping("")
    public ResponseEntity<List<EquipmentTypeDto>> getAllEquipmentTypes() {
        return ResponseEntity.status(HttpStatus.OK).body(this.equipmentTypeService.getAllEquipmentTypes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipmentTypeDto> getEquipmentType(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.equipmentTypeService.getEquipmentType(id));
    }

    @PostMapping
    public ResponseEntity<EquipmentTypeDto> createEquipmentType(@Valid @RequestBody EquipmentTypeSaveDto dto) {
        EquipmentTypeDto result = this.equipmentTypeService.createEquipmentType(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EquipmentTypeDto> updateEquipmentType(@PathVariable("id") Long id, @Valid @RequestBody EquipmentTypeSaveDto dto) {
        EquipmentTypeDto result = this.equipmentTypeService.updateEquipmentType(id, dto);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEquipmentType(@PathVariable("id") Long id) {
        this.equipmentTypeService.deleteEquipmentType(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
