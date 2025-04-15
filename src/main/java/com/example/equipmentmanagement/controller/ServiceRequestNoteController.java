package com.example.equipmentmanagement.controller;

import com.example.equipmentmanagement.dto.servicerequest.ServiceRequestNoteDto;
import com.example.equipmentmanagement.dto.servicerequest.ServiceRequestNoteSaveDto;
import com.example.equipmentmanagement.service.ServiceRequestNoteService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/service-request-note")
public class ServiceRequestNoteController {

    private final ServiceRequestNoteService serviceRequestNoteService;

    public ServiceRequestNoteController(ServiceRequestNoteService serviceRequestNoteService) {
        this.serviceRequestNoteService = serviceRequestNoteService;
    }

    @GetMapping()
    public ResponseEntity<List<ServiceRequestNoteDto>> getAllServiceRequestNotes() {
        return ResponseEntity.ok(this.serviceRequestNoteService.getAllServiceRequestNotes());
    }

    @GetMapping("/service-request/{service-request-id}")
    public ResponseEntity<Page<ServiceRequestNoteDto>> getNotesByServiceRequest(
            @PathVariable("service-request-id") Long serviceRequestId,
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize
    ) {
        int page = pageNumber != null && pageNumber >= 0 ? pageNumber : 0;
        int size = pageSize != null && pageSize >= 0 ? pageSize : 10;

        return ResponseEntity.ok(this.serviceRequestNoteService.getNotesByServiceRequest(serviceRequestId, page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceRequestNoteDto> getServiceRequestNote(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.serviceRequestNoteService.getServiceRequestNote(id));
    }

    @PostMapping
    public ResponseEntity<ServiceRequestNoteDto> createServiceRequestNote(@Valid @RequestBody ServiceRequestNoteSaveDto dto) {
        ServiceRequestNoteDto result = this.serviceRequestNoteService.createServiceRequestNote(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceRequestNoteDto> updateServiceRequestNote(@PathVariable("id") Long id, @Valid @RequestBody ServiceRequestNoteSaveDto dto) {
        ServiceRequestNoteDto result = this.serviceRequestNoteService.updateServiceRequestNote(id, dto);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServiceRequestNote(@PathVariable("id") Long id) {
        this.serviceRequestNoteService.deleteServiceRequestNote(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
