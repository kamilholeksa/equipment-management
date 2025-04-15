package com.example.equipmentmanagement.service;

import com.example.equipmentmanagement.dto.equipment.EquipmentHistoryDto;
import com.example.equipmentmanagement.mapper.EquipmentHistoryMapper;
import com.example.equipmentmanagement.model.Equipment;
import com.example.equipmentmanagement.model.EquipmentHistory;
import com.example.equipmentmanagement.repository.EquipmentHistoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Service
public class EquipmentHistoryService {

    private final EquipmentHistoryRepository repository;

    public EquipmentHistoryService(EquipmentHistoryRepository repository) {
        this.repository = repository;
    }

    public List<EquipmentHistoryDto> getHistoryByEquipmentId(Long id) {
        return this.repository.findByEquipmentId(id).stream()
                .map(EquipmentHistoryMapper::toDto)
                .toList();
    }

    @Transactional
    protected void saveEquipmentHistory(Equipment oldEquipment, Equipment newEquipment) {
        if (
                !Objects.equals(oldEquipment.getStatus(), newEquipment.getStatus()) ||
                !Objects.equals(oldEquipment.getLocation(), newEquipment.getLocation()) ||
                !Objects.equals(oldEquipment.getAddress(), newEquipment.getAddress()) ||
                !Objects.equals(oldEquipment.getUser(), newEquipment.getUser())
        ) {
            String oldLocation = oldEquipment.getAddress() == null
                    ? oldEquipment.getLocation()
                    : oldEquipment.getLocation() + ", " +
                    oldEquipment.getAddress().getPostalCode() + " " +
                    oldEquipment.getAddress().getCity() + ", " +
                    oldEquipment.getAddress().getStreet() + " " +
                    oldEquipment.getAddress().getNumber();

            String newLocation = newEquipment.getAddress() == null
                    ? newEquipment.getLocation()
                    : newEquipment.getLocation() + ", " +
                    newEquipment.getAddress().getPostalCode() + " " +
                    newEquipment.getAddress().getCity() + ", " +
                    newEquipment.getAddress().getStreet() + " " +
                    newEquipment.getAddress().getNumber();

            EquipmentHistory history = new EquipmentHistory();
            history.setEquipment(newEquipment);
            history.setOldStatus(oldEquipment.getStatus());
            history.setNewStatus(newEquipment.getStatus());
            history.setOldLocation(oldLocation);
            history.setNewLocation(newLocation);
            history.setOldUser(oldEquipment.getUser());
            history.setNewUser(newEquipment.getUser());
            history.setTimestamp(Instant.now());

            repository.save(history);
        }
    }
}
