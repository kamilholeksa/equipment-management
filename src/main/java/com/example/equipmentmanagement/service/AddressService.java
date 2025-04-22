package com.example.equipmentmanagement.service;

import com.example.equipmentmanagement.dto.address.AddressDto;
import com.example.equipmentmanagement.dto.address.AddressSaveDto;
import com.example.equipmentmanagement.exception.BadRequestAlertException;
import com.example.equipmentmanagement.exception.ResourceNotFoundException;
import com.example.equipmentmanagement.mapper.AddressMapper;
import com.example.equipmentmanagement.model.Address;
import com.example.equipmentmanagement.repository.AddressRepository;
import com.example.equipmentmanagement.repository.EquipmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final EquipmentRepository equipmentRepository;

    public AddressService(AddressRepository addressRepository, AddressMapper addressMapper, EquipmentRepository equipmentRepository) {
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
        this.equipmentRepository = equipmentRepository;
    }

    public List<AddressDto> getAllAddresses() {
        return this.addressRepository.findAll().stream()
                .map(addressMapper::addressToAddressDto)
                .toList();
    }

    public AddressDto getAddress(Long id) {
        return addressMapper.addressToAddressDto(findAddressById(id));
    }

    @Transactional
    public AddressDto createAddress(AddressSaveDto dto) {
        Address address = addressMapper.addressSaveDtoToAddress(dto);
        return addressMapper.addressToAddressDto(this.addressRepository.save(address));
    }

    @Transactional
    public AddressDto updateAddress(Long id, AddressSaveDto dto) {
        Address existingAddress = findAddressById(id);
        Address updatedAddress = addressMapper.addressSaveDtoToAddress(dto);

        updatedAddress.setId(existingAddress.getId());

        return addressMapper.addressToAddressDto(addressRepository.save(updatedAddress));
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
