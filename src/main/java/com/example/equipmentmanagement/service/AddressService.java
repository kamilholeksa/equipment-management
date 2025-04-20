package com.example.equipmentmanagement.service;

import com.example.equipmentmanagement.dto.address.AddressDto;
import com.example.equipmentmanagement.dto.address.AddressSaveDto;
import com.example.equipmentmanagement.exception.BadRequestAlertException;
import com.example.equipmentmanagement.mapper.AddressMapper;
import com.example.equipmentmanagement.exception.ResourceNotFoundException;
import com.example.equipmentmanagement.model.Address;
import com.example.equipmentmanagement.repository.AddressRepository;
import com.example.equipmentmanagement.repository.EquipmentRepository;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.equipmentmanagement.mapper.AddressMapper.toDto;
import static com.example.equipmentmanagement.mapper.AddressMapper.toEntity;

@Service
public class AddressService {
    private final AddressRepository addressRepository;
    private final EquipmentRepository equipmentRepository;

    public AddressService(AddressRepository addressRepository, EquipmentRepository equipmentRepository) {
        this.addressRepository = addressRepository;
        this.equipmentRepository = equipmentRepository;
    }

    public List<AddressDto> getAllAddresses() {
        return this.addressRepository.findAll().stream()
                .map(AddressMapper::toDto)
                .toList();
    }

    public AddressDto getAddress(Long id) {
        return toDto(findAddressById(id));
    }

    @Transactional
    public AddressDto createAddress(AddressSaveDto dto) {
        return toDto(this.addressRepository.save(toEntity(dto)));
    }

    @Transactional
    public AddressDto updateAddress(Long id, AddressSaveDto dto) {
        Address existingAddress = findAddressById(id);
        Address updatedAddress = toEntity(dto);

        updatedAddress.setId(existingAddress.getId());

        return toDto(addressRepository.save(updatedAddress));
    }

    @Transactional
    public void deleteAddress(Long id) {
        Address existingAddress = findAddressById(id);
        int count = equipmentRepository.countByAddressId(id);
        if (count > 0) {
            throw new BadRequestAlertException("Failed. There are equipment with this address: " + count);
        }

        this.addressRepository.delete(existingAddress);
    }

    private Address findAddressById(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address", id));
    }
}
