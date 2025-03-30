package com.example.equipmentmanagement.service;

import com.example.equipmentmanagement.dto.AddressDto;
import com.example.equipmentmanagement.dto.AddressSaveDto;
import com.example.equipmentmanagement.dto.mapper.AddressMapper;
import com.example.equipmentmanagement.exception.ResourceNotFoundException;
import com.example.equipmentmanagement.model.Address;
import com.example.equipmentmanagement.repository.AddressRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.equipmentmanagement.dto.mapper.AddressMapper.toDto;
import static com.example.equipmentmanagement.dto.mapper.AddressMapper.toEntity;

@Service
public class AddressService {
    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
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
        this.addressRepository.delete(existingAddress);
    }

    private Address findAddressById(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address", id));
    }
}
