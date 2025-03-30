package com.example.equipmentmanagement.service;

import com.example.equipmentmanagement.dto.ServiceRequestDto;
import com.example.equipmentmanagement.dto.ServiceRequestSaveDto;
import com.example.equipmentmanagement.dto.ServiceRequestWithNotesDto;
import com.example.equipmentmanagement.dto.mapper.ServiceRequestMapper;
import com.example.equipmentmanagement.exception.ResourceNotFoundException;
import com.example.equipmentmanagement.model.Equipment;
import com.example.equipmentmanagement.model.ServiceRequest;
import com.example.equipmentmanagement.model.User;
import com.example.equipmentmanagement.model.enumeration.EquipmentStatus;
import com.example.equipmentmanagement.model.enumeration.ServiceRequestStatus;
import com.example.equipmentmanagement.repository.EquipmentRepository;
import com.example.equipmentmanagement.repository.ServiceRequestRepository;
import com.example.equipmentmanagement.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import static com.example.equipmentmanagement.dto.mapper.ServiceRequestMapper.*;

@Service
public class ServiceRequestService {

    private final UserService userService;
    private final ServiceRequestRepository serviceRequestRepository;
    private final EquipmentRepository equipmentRepository;
    private final UserRepository userRepository;

    public ServiceRequestService(UserService userService, ServiceRequestRepository serviceRequestRepository, EquipmentRepository equipmentRepository, UserRepository userRepository) {
        this.userService = userService;
        this.serviceRequestRepository = serviceRequestRepository;
        this.equipmentRepository = equipmentRepository;
        this.userRepository = userRepository;
    }

    public Page<ServiceRequestDto> getAllServiceRequests(
            int pageNumber,
            int pageSize,
            String sortField,
            String sortOrder
    ) {
        Sort sort = Sort.by(sortOrder.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortField);
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        Page<Long> idsPage = serviceRequestRepository.findAllIds(pageRequest);
        Set<Long> ids = Set.copyOf(idsPage.getContent());

        List<ServiceRequestDto> serviceRequests = serviceRequestRepository.findAllByIdIn(ids, pageRequest.getSort()).stream()
                .map(ServiceRequestMapper::toDto)
                .toList();

        return new PageImpl<>(serviceRequests, pageRequest, idsPage.getTotalElements());
    }

    public ServiceRequestDto getServiceRequest(Long id) {
        return toDto(findServiceRequestById(id));
    }

    public ServiceRequestWithNotesDto getServiceRequestWithNotes(Long id) {
        return toDtoWithNotes(findServiceRequestById(id));
    }

    public Page<ServiceRequestDto> getServiceRequestsByEquipment(
            Long equipmentId,
            int pageNumber,
            int pageSize,
            String sortField,
            String sortOrder
    ) {
        Sort sort = Sort.by(sortOrder.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortField);
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        return this.serviceRequestRepository.findAllByEquipmentId(equipmentId, pageRequest).map(ServiceRequestMapper::toDto);
    }

    public Page<ServiceRequestDto> getOpenServiceRequests(
            int pageNumber,
            int pageSize,
            String sortField,
            String sortOrder
    ) {
        Sort sort = Sort.by(sortOrder.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortField);
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        Set<ServiceRequestStatus> statuses = Set.of(
                ServiceRequestStatus.NEW,
                ServiceRequestStatus.ACCEPTED,
                ServiceRequestStatus.IN_PROGRESS
        );

        return this.serviceRequestRepository.findAllByStatusIn(statuses, pageRequest).map(ServiceRequestMapper::toDto);
    }

    @Transactional
    public ServiceRequestDto createServiceRequest(ServiceRequestSaveDto dto) {
        Equipment equipment = findEquipmentById(dto.getEquipmentId());
        User user = userService.getCurrentUserEntity();

        dto.setStatus(ServiceRequestStatus.NEW.name());

        return toDto(this.serviceRequestRepository.save(toEntity(dto, equipment, user, null)));
    }

    @Transactional
    public ServiceRequestDto updateServiceRequest(Long id, ServiceRequestSaveDto dto) {
        ServiceRequest existingServiceRequest = findServiceRequestById(id);
        Equipment equipment = findEquipmentById(dto.getEquipmentId());
        User user = findUserById(dto.getUserId());
        User technician = findUserById(dto.getTechnicianId());

        ServiceRequest updatedServiceRequest = toEntity(dto, equipment, user, technician);
        updatedServiceRequest.setId(existingServiceRequest.getId());

        return toDto(serviceRequestRepository.save(updatedServiceRequest));
    }

    @Transactional
    public void deleteServiceRequest(Long id) {
        ServiceRequest existingServiceRequest = findServiceRequestById(id);
        this.serviceRequestRepository.delete(existingServiceRequest);
    }

    @Transactional
    public void accept(Long id) {
        ServiceRequest serviceRequest = findServiceRequestById(id);
        Equipment equipment = serviceRequest.getEquipment();

        if (serviceRequest.getStatus() != ServiceRequestStatus.ACCEPTED) {
            serviceRequest.setStatus(ServiceRequestStatus.ACCEPTED);
        }

        if (equipment.getStatus() != EquipmentStatus.IN_REPAIR) {
            equipment.setStatus(EquipmentStatus.IN_REPAIR);
            equipmentRepository.save(equipment);
        }
    }

    @Transactional
    public void close(Long id, String closeInfo) {
        ServiceRequest serviceRequest = findServiceRequestById(id);
        Equipment equipment = serviceRequest.getEquipment();

        if (serviceRequest.getStatus() != ServiceRequestStatus.CLOSED) {
            serviceRequest.setStatus(ServiceRequestStatus.CLOSED);
            serviceRequest.setCloseInfo(closeInfo);
        }

        if (equipment.getStatus() != EquipmentStatus.IN_USE) {
            equipment.setStatus(EquipmentStatus.IN_USE);
            equipmentRepository.save(equipment);
        }
    }

    @Transactional
    public void assignTechnician(Long id, Long userId) {
        ServiceRequest serviceRequest = findServiceRequestById(id);
        User technician = findUserById(userId);
        serviceRequest.setTechnician(technician);
        serviceRequest.setStatus(ServiceRequestStatus.IN_PROGRESS);
    }

    @Transactional
    public void cancel(Long id) {
        ServiceRequest serviceRequest = findServiceRequestById(id);

        if (serviceRequest.getStatus() == ServiceRequestStatus.NEW) {
            serviceRequest.setStatus(ServiceRequestStatus.CANCELLED);
        } else {
            throw new IllegalArgumentException("Cannot cancel ServiceRequest with '" + serviceRequest.getStatus().name() + "' status");
        }
    }

    private ServiceRequest findServiceRequestById(Long id) {
        return serviceRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ServiceRequest", id));
    }

    private Equipment findEquipmentById(Long id) {
        return equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment", id));
    }

    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
    }
}
