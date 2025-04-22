package com.example.equipmentmanagement.service;

import com.example.equipmentmanagement.dto.servicerequest.ServiceRequestNoteDto;
import com.example.equipmentmanagement.dto.servicerequest.ServiceRequestNoteSaveDto;
import com.example.equipmentmanagement.exception.AccessDeniedException;
import com.example.equipmentmanagement.exception.ResourceNotFoundException;
import com.example.equipmentmanagement.mapper.ServiceRequestNoteMapper;
import com.example.equipmentmanagement.model.ServiceRequestNote;
import com.example.equipmentmanagement.repository.ServiceRequestNoteRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceRequestNoteService {

    private final ServiceRequestNoteRepository serviceRequestNoteRepository;
    private final ServiceRequestNoteMapper serviceRequestNoteMapper;
    private final UserService userService;

    public ServiceRequestNoteService(ServiceRequestNoteRepository serviceRequestNoteRepository, ServiceRequestNoteMapper serviceRequestNoteMapper, UserService userService) {
        this.serviceRequestNoteRepository = serviceRequestNoteRepository;
        this.serviceRequestNoteMapper = serviceRequestNoteMapper;
        this.userService = userService;
    }

    public List<ServiceRequestNoteDto> getAllServiceRequestNotes() {
        return this.serviceRequestNoteRepository.findAll().stream()
                .map(serviceRequestNoteMapper::noteToNoteDto)
                .toList();
    }

    public Page<ServiceRequestNoteDto> getNotesByServiceRequest(Long serviceRequestId, Pageable pageable) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return this.serviceRequestNoteRepository.findByServiceRequestId(serviceRequestId, pageRequest).map(serviceRequestNoteMapper::noteToNoteDto);
    }

    public ServiceRequestNoteDto getServiceRequestNote(Long id) {
        return serviceRequestNoteMapper.noteToNoteDto(findServiceRequestNoteById(id));
    }

    @Transactional
    public ServiceRequestNoteDto createServiceRequestNote(ServiceRequestNoteSaveDto dto) {
        return serviceRequestNoteMapper.noteToNoteDto(this.serviceRequestNoteRepository.save(serviceRequestNoteMapper.noteSaveDtoToNote(dto)));
    }

    @Transactional
    public ServiceRequestNoteDto updateServiceRequestNote(Long id, ServiceRequestNoteSaveDto dto) {
        ServiceRequestNote existingNote = findServiceRequestNoteById(id);

        if (!existingNote.getCreatedBy().equals(userService.getCurrentUser().getUsername())) {
            throw new AccessDeniedException("Failed to update the note: access denied");
        }

        ServiceRequestNote updatedServiceRequestNote = serviceRequestNoteMapper.noteSaveDtoToNote(dto);
        updatedServiceRequestNote.setId(existingNote.getId());

        return serviceRequestNoteMapper.noteToNoteDto(serviceRequestNoteRepository.save(updatedServiceRequestNote));
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
}
