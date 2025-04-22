package com.example.equipmentmanagement.service;

import com.example.equipmentmanagement.dto.servicerequest.ServiceRequestDto;
import com.example.equipmentmanagement.dto.servicerequest.ServiceRequestSaveDto;
import com.example.equipmentmanagement.dto.servicerequest.ServiceRequestWithNotesDto;
import com.example.equipmentmanagement.enumeration.EquipmentStatus;
import com.example.equipmentmanagement.enumeration.ServiceRequestStatus;
import com.example.equipmentmanagement.exception.ResourceNotFoundException;
import com.example.equipmentmanagement.mapper.ServiceRequestMapper;
import com.example.equipmentmanagement.model.Equipment;
import com.example.equipmentmanagement.model.ServiceRequest;
import com.example.equipmentmanagement.model.User;
import com.example.equipmentmanagement.repository.EquipmentRepository;
import com.example.equipmentmanagement.repository.ServiceRequestRepository;
import com.example.equipmentmanagement.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ServiceRequestService {

    private final ServiceRequestRepository serviceRequestRepository;
    private final ServiceRequestMapper serviceRequestMapper;
    private final EquipmentRepository equipmentRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public ServiceRequestService(ServiceRequestRepository serviceRequestRepository, ServiceRequestMapper serviceRequestMapper, EquipmentRepository equipmentRepository, UserRepository userRepository, UserService userService) {
        this.serviceRequestRepository = serviceRequestRepository;
        this.serviceRequestMapper = serviceRequestMapper;
        this.equipmentRepository = equipmentRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public Page<ServiceRequestDto> getAllServiceRequests(Pageable pageable) {
        return serviceRequestRepository.findAll(pageable).map(serviceRequestMapper::serviceRequestToServiceRequestDto);
    }

    public ServiceRequestDto getServiceRequest(Long id) {
        return serviceRequestMapper.serviceRequestToServiceRequestDto(findServiceRequestById(id));
    }

    public ServiceRequestWithNotesDto getServiceRequestWithNotes(Long id) {
        return serviceRequestMapper.serviceRequestToServiceRequestWithNotesDto(findServiceRequestById(id));
    }

    public Page<ServiceRequestDto> getServiceRequestsByEquipment(Long equipmentId, Pageable pageable) {
        return this.serviceRequestRepository.findAllByEquipmentId(equipmentId, pageable).map(serviceRequestMapper::serviceRequestToServiceRequestDto);
    }

    public Page<ServiceRequestDto> getOpenServiceRequests(Pageable pageable) {
        Set<ServiceRequestStatus> statuses = Set.of(
                ServiceRequestStatus.NEW,
                ServiceRequestStatus.ACCEPTED,
                ServiceRequestStatus.IN_PROGRESS
        );

        return this.serviceRequestRepository.findAllByStatusIn(statuses, pageable).map(serviceRequestMapper::serviceRequestToServiceRequestDto);
    }

    @Transactional
    public ServiceRequestDto createServiceRequest(ServiceRequestSaveDto dto) {
        User user = userService.getCurrentUserEntity();
        dto.setUserId(user.getId());
        dto.setStatus(ServiceRequestStatus.NEW.name());

        ServiceRequest serviceRequest = serviceRequestMapper.serviceRequestSaveDtoToServiceRequest(dto);

        return serviceRequestMapper.serviceRequestToServiceRequestDto(this.serviceRequestRepository.save(serviceRequest));
    }

    @Transactional
    public ServiceRequestDto updateServiceRequest(Long id, ServiceRequestSaveDto dto) {
        ServiceRequest existingServiceRequest = findServiceRequestById(id);

        ServiceRequest updatedServiceRequest = serviceRequestMapper.serviceRequestSaveDtoToServiceRequest(dto);
        updatedServiceRequest.setId(existingServiceRequest.getId());

        return serviceRequestMapper.serviceRequestToServiceRequestDto(serviceRequestRepository.save(updatedServiceRequest));
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

    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
    }
}
