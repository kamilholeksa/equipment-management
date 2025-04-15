package com.example.equipmentmanagement.service;

import com.example.equipmentmanagement.dto.servicerequest.ServiceRequestNoteDto;
import com.example.equipmentmanagement.dto.servicerequest.ServiceRequestNoteSaveDto;
import com.example.equipmentmanagement.mapper.ServiceRequestNoteMapper;
import com.example.equipmentmanagement.exception.AccessDeniedException;
import com.example.equipmentmanagement.exception.ResourceNotFoundException;
import com.example.equipmentmanagement.model.ServiceRequest;
import com.example.equipmentmanagement.model.ServiceRequestNote;
import com.example.equipmentmanagement.repository.ServiceRequestNoteRepository;
import com.example.equipmentmanagement.repository.ServiceRequestRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.equipmentmanagement.mapper.ServiceRequestNoteMapper.toDto;
import static com.example.equipmentmanagement.mapper.ServiceRequestNoteMapper.toEntity;

@Service
public class ServiceRequestNoteService {

    private final ServiceRequestNoteRepository serviceRequestNoteRepository;
    private final ServiceRequestRepository serviceRequestRepository;
    private final UserService userService;

    public ServiceRequestNoteService(ServiceRequestNoteRepository serviceRequestNoteRepository, ServiceRequestRepository serviceRequestRepository, UserService userService) {
        this.serviceRequestNoteRepository = serviceRequestNoteRepository;
        this.serviceRequestRepository = serviceRequestRepository;
        this.userService = userService;
    }

    public List<ServiceRequestNoteDto> getAllServiceRequestNotes() {
        return this.serviceRequestNoteRepository.findAll().stream()
                .map(ServiceRequestNoteMapper::toDto)
                .toList();
    }

    public Page<ServiceRequestNoteDto> getNotesByServiceRequest(
            Long serviceRequestId,
            int pageNumber,
            int pageSize
    ) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);
        return this.serviceRequestNoteRepository.findByServiceRequestId(serviceRequestId, pageRequest).map(ServiceRequestNoteMapper::toDto);
    }

    public ServiceRequestNoteDto getServiceRequestNote(Long id) {
        return toDto(findServiceRequestNoteById(id));
    }

    @Transactional
    public ServiceRequestNoteDto createServiceRequestNote(ServiceRequestNoteSaveDto dto) {
        ServiceRequest serviceRequest = findServiceRequestById(dto.getServiceRequestId());
        return toDto(this.serviceRequestNoteRepository.save(toEntity(dto, serviceRequest)));
    }

    @Transactional
    public ServiceRequestNoteDto updateServiceRequestNote(Long id, ServiceRequestNoteSaveDto dto) {
        ServiceRequestNote existingNote = findServiceRequestNoteById(id);

        if (!existingNote.getCreatedBy().equals(userService.getCurrentUser().getUsername())) {
            throw new AccessDeniedException("Failed to update the note: access denied");
        }

        ServiceRequest serviceRequest = findServiceRequestById(dto.getServiceRequestId());

        ServiceRequestNote updatedServiceRequestNote = toEntity(dto, serviceRequest);
        updatedServiceRequestNote.setId(existingNote.getId());

        return toDto(serviceRequestNoteRepository.save(updatedServiceRequestNote));
    }

    @Transactional
    public void deleteServiceRequestNote(Long id) {
        ServiceRequestNote existingNote = findServiceRequestNoteById(id);

        if (!existingNote.getCreatedBy().equals(userService.getCurrentUser().getUsername())) {
            throw new AccessDeniedException("Failed to delete the note: access denied");
        }

        this.serviceRequestNoteRepository.delete(existingNote);
    }

    private ServiceRequestNote findServiceRequestNoteById(Long id) {
        return serviceRequestNoteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ServiceRequestNote", id));
    }

    private ServiceRequest findServiceRequestById(Long id) {
        return serviceRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ServiceRequest", id));
    }
}
