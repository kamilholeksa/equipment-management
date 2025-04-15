package com.example.equipmentmanagement.controller;

import com.example.equipmentmanagement.dto.servicerequest.ServiceRequestDto;
import com.example.equipmentmanagement.dto.servicerequest.ServiceRequestSaveDto;
import com.example.equipmentmanagement.dto.servicerequest.ServiceRequestWithNotesDto;
import com.example.equipmentmanagement.service.ServiceRequestService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/service-request")
public class ServiceRequestController {

    private final ServiceRequestService serviceRequestService;

    public ServiceRequestController(ServiceRequestService serviceRequestService) {
        this.serviceRequestService = serviceRequestService;
    }

    @GetMapping("")
    public ResponseEntity<Page<ServiceRequestDto>> getAllServiceRequests(
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false, defaultValue = "id") String sortField,
            @RequestParam(required = false, defaultValue = "desc") String sortOrder
    ) {
        int page = pageNumber != null && pageNumber >= 0 ? pageNumber : 0;
        int size = pageSize != null && pageSize >= 0 ? pageSize : 10;

        return ResponseEntity.ok(this.serviceRequestService.getAllServiceRequests(page, size, sortField, sortOrder));
    }

    @GetMapping("/equipment/{equipmentId}")
    public ResponseEntity<Page<ServiceRequestDto>> getServiceRequestsByEquipment(
            @PathVariable("equipmentId") Long equipmentId,
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false, defaultValue = "id") String sortField,
            @RequestParam(required = false, defaultValue = "desc") String sortOrder
    ) {
        int page = pageNumber != null && pageNumber >= 0 ? pageNumber : 0;
        int size = pageSize != null && pageSize >= 0 ? pageSize : 10;

        return ResponseEntity.ok(this.serviceRequestService.getServiceRequestsByEquipment(equipmentId, page, size, sortField, sortOrder));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceRequestDto> getServiceRequest(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.serviceRequestService.getServiceRequest(id));
    }

    @GetMapping("/{id}/with-notes")
    public ResponseEntity<ServiceRequestWithNotesDto> getServiceRequestWithNotes(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.serviceRequestService.getServiceRequestWithNotes(id));
    }

    @GetMapping("/open")
    public ResponseEntity<Page<ServiceRequestDto>> getOpenServiceRequests(
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false, defaultValue = "id") String sortField,
            @RequestParam(required = false, defaultValue = "desc") String sortOrder
    ) {
        int page = pageNumber != null && pageNumber >= 0 ? pageNumber : 0;
        int size = pageSize != null && pageSize >= 0 ? pageSize : 10;

        return ResponseEntity.ok(this.serviceRequestService.getOpenServiceRequests(page, size, sortField, sortOrder));
    }

    @PostMapping
    public ResponseEntity<ServiceRequestDto> createServiceRequest(@Valid @RequestBody ServiceRequestSaveDto dto) {
        ServiceRequestDto result = this.serviceRequestService.createServiceRequest(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceRequestDto> updateServiceRequest(@PathVariable("id") Long id, @Valid @RequestBody ServiceRequestSaveDto dto) {
        ServiceRequestDto result = this.serviceRequestService.updateServiceRequest(id, dto);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PatchMapping("/{id}/accept")
    public ResponseEntity<ResponseMessage> acceptServiceRequest(@PathVariable("id") Long id) {
        serviceRequestService.accept(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Service request accepted"));
    }

    @PatchMapping("/{id}/close")
    public ResponseEntity<ResponseMessage> closeServiceRequest(@PathVariable("id") Long id, @RequestBody String closeInfo) {
        serviceRequestService.close(id, closeInfo);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Service request closed successfully"));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ResponseMessage> cancelServiceRequest(@PathVariable("id") Long id) {
        serviceRequestService.cancel(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Service request cancelled successfully"));
    }

    @PatchMapping("/{id}/assign")
    public ResponseEntity<ResponseMessage> assignTechnician(@PathVariable("id") Long id, @RequestBody Long userId) {
        serviceRequestService.assignTechnician(id, userId);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("New technician assigned successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServiceRequest(@PathVariable("id") Long id) {
        this.serviceRequestService.deleteServiceRequest(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
