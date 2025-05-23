package com.example.equipmentmanagement.controller;

import com.example.equipmentmanagement.dto.equipment.EquipmentDto;
import com.example.equipmentmanagement.dto.equipment.EquipmentFilter;
import com.example.equipmentmanagement.dto.equipment.EquipmentSaveDto;
import com.example.equipmentmanagement.service.EquipmentService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/equipment")
public class EquipmentController {

    private final EquipmentService equipmentService;

    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @GetMapping("")
    public ResponseEntity<Page<EquipmentDto>> getAllEquipment(EquipmentFilter filter, Pageable pageable) {
        return ResponseEntity.ok(this.equipmentService.getAllEquipment(filter, pageable));
    }

    @GetMapping("/my-equipment")
    public ResponseEntity<Page<EquipmentDto>> getCurrentUserEquipment(EquipmentFilter filter, Pageable pageable) {
        return ResponseEntity.ok(this.equipmentService.getCurrentUserEquipment(filter, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipmentDto> getEquipment(@PathVariable("id") Long id) {
        return ResponseEntity.ok(this.equipmentService.getEquipment(id));
    }

    @PostMapping()
    public ResponseEntity<EquipmentDto> createEquipment(@Valid @RequestBody EquipmentSaveDto dto) {
        EquipmentDto result = this.equipmentService.createEquipment(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EquipmentDto> updateEquipment(@PathVariable("id") Long id, @Valid @RequestBody EquipmentSaveDto dto) {
        EquipmentDto result = this.equipmentService.updateEquipment(id, dto);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEquipment(@PathVariable("id") Long id) {
        this.equipmentService.deleteEquipment(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{id}/decommission")
    public ResponseEntity<ResponseMessage> decommissionEquipment(@PathVariable("id") Long id) {
        this.equipmentService.decommission(id);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseMessage.text("Equipment decommissioned successfully"));
    }

}
