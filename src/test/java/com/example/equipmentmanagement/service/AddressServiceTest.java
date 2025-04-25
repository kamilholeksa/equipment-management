package com.example.equipmentmanagement.service;

import com.example.equipmentmanagement.dto.address.AddressDto;
import com.example.equipmentmanagement.dto.address.AddressSaveDto;
import com.example.equipmentmanagement.exception.BadRequestAlertException;
import com.example.equipmentmanagement.exception.ResourceNotFoundException;
import com.example.equipmentmanagement.mapper.AddressMapper;
import com.example.equipmentmanagement.model.Address;
import com.example.equipmentmanagement.repository.AddressRepository;
import com.example.equipmentmanagement.repository.EquipmentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    @InjectMocks
    AddressService addressService;

    @Mock
    AddressRepository addressRepository;
    @Mock
    EquipmentRepository equipmentRepository;
    @Mock
    AddressMapper addressMapper;

    @DisplayName("List of AddressDto objects returned")
    @Test
    void testGetAllAddresses_shouldReturnListOfAddressDto() {
        // Given
        Address address = new Address();
        AddressDto dto = new AddressDto();

        when(addressRepository.findAll()).thenReturn(List.of(address));
        when(addressMapper.addressToAddressDto(any(Address.class))).thenReturn(dto);

        // When
        List<AddressDto> result = addressService.getAllAddresses();

        // Then
        assertEquals(1, result.size());
        assertEquals(dto, result.getFirst());
        verify(addressRepository).findAll();
    }

    @DisplayName("AddressDto object returned")
    @Test
    void testGetAddress_whenAddressExists_shouldReturnAddressDto() {
        // Given
        Long addressId = 1L;
        Address address = new Address();
        AddressDto dto = new AddressDto();

        when(addressRepository.findById(anyLong())).thenReturn(Optional.of(address));
        when(addressMapper.addressToAddressDto(any(Address.class))).thenReturn(dto);

        // When
        AddressDto result = addressService.getAddress(addressId);

        // Then
        assertEquals(dto, result);
        verify(addressRepository).findById(addressId);
    }

    @DisplayName("ResourceNotFoundException thrown")
    @Test
    void testGetAddress_whenAddressNotFound_shouldThrowResourceNotFoundException() {
        // Given
        Long addressId = 1L;
        String expectedMessage = "Address with id 1 not found";
        when(addressRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> addressService.getAddress(addressId));
        assertEquals(expectedMessage, exception.getMessage());
        verify(addressRepository, never()).save(any(Address.class));
    }

    @DisplayName("Address object created")
    @Test
    void testCreateAddress_whenAddressSaveDtoProvided_shouldReturnAddressDto() {
        // Given
        Long addressId = 1L;

        Address address = new Address();
        address.setId(addressId);
        address.setPostalCode("12-345");
        address.setCity("City");
        address.setStreet("Street");
        address.setNumber("21");
        address.setDescription("Description");

        AddressDto addressDto = new AddressDto();
        addressDto.setId(addressId);
        addressDto.setPostalCode("12-345");
        addressDto.setCity("City");
        addressDto.setStreet("Street");
        addressDto.setNumber("21");
        addressDto.setDescription("Description");

        when(addressRepository.save(any(Address.class))).thenReturn(address);
        when(addressMapper.addressSaveDtoToAddress(any(AddressSaveDto.class))).thenReturn(address);
        when(addressMapper.addressToAddressDto(any(Address.class))).thenReturn(addressDto);

        // When
        AddressDto result = addressService.createAddress(new AddressSaveDto());

        // Then
        assertEquals(addressDto, result);
        verify(addressRepository).save(any(Address.class));
    }

    @DisplayName("Address object updated")
    @Test
    void testUpdateAddress_whenExists_shouldUpdateAndReturnAddressDto() {
        // Given
        Long addressId = 1L;

        Address existingAddress = new Address();
        existingAddress.setId(addressId);
        existingAddress.setPostalCode("12-345");
        existingAddress.setCity("City");
        existingAddress.setStreet("Street");
        existingAddress.setNumber("21");
        existingAddress.setDescription("Description");

        AddressSaveDto requestDto = new AddressSaveDto();
        requestDto.setPostalCode("11-111");
        requestDto.setCity("City1");
        requestDto.setStreet("Street1");
        requestDto.setNumber("22");
        requestDto.setDescription("Description1");

        Address savedAddress = new Address();
        savedAddress.setId(addressId);
        savedAddress.setPostalCode("11-111");
        savedAddress.setCity("City1");
        savedAddress.setStreet("Street1");
        savedAddress.setNumber("22");
        savedAddress.setDescription("Description1");

        AddressDto addressDto = new AddressDto();
        addressDto.setId(addressId);
        addressDto.setPostalCode("11-111");
        addressDto.setCity("City1");
        addressDto.setStreet("Street1");
        addressDto.setNumber("22");
        addressDto.setDescription("Description1");

        when(addressRepository.findById(anyLong())).thenReturn(Optional.of(existingAddress));
        when(addressRepository.save(any(Address.class))).thenReturn(savedAddress);
        when(addressMapper.addressSaveDtoToAddress(any(AddressSaveDto.class))).thenReturn(savedAddress);
        when(addressMapper.addressToAddressDto(any(Address.class))).thenReturn(addressDto);

        // When
        AddressDto result = addressService.updateAddress(addressId, requestDto);

        // Then
        assertEquals(addressDto, result);
        verify(addressRepository).save(any(Address.class));
    }

    @DisplayName("ResourceNotFoundException thrown")
    @Test
    void testUpdateAddress_whenNotFound_shouldThrowResourceNotFoundException() {
        // Given
        Long addressId = 1L;

        AddressSaveDto requestDto = new AddressSaveDto();
        String expectedMessage = "Address with id 1 not found";

        when(addressRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> addressService.updateAddress(addressId, requestDto));
        assertEquals(expectedMessage, exception.getMessage());
        verify(addressRepository, never()).save(any(Address.class));
    }

    @DisplayName("Address deleted")
    @Test
    void testDeleteAddress_whenNoEquipmentLinked_shouldDeleteAddress() {
        // Given
        Long addressId = 1L;

        Address existingAddress = new Address();
        existingAddress.setId(addressId);

        when(addressRepository.findById(anyLong())).thenReturn(Optional.of(existingAddress));
        when(equipmentRepository.countByAddressId(anyLong())).thenReturn(0);

        // When
        addressService.deleteAddress(addressId);

        // Then
        verify(addressRepository).delete(existingAddress);
    }

    @DisplayName("BadRequestAlertException thrown when there is Equipment linked to it")
    @Test
    void testDeleteAddress_whenEquipmentLinked_shouldThrowBadRequestAlertException() {
        // Given
        Long addressId = 1L;

        Address existingAddress = new Address();
        existingAddress.setId(addressId);
        String expectedMessage = "Failed. There are equipment with this address: 1";

        when(addressRepository.findById(anyLong())).thenReturn(Optional.of(existingAddress));
        when(equipmentRepository.countByAddressId(anyLong())).thenReturn(1);

        // When & Then
        BadRequestAlertException exception = assertThrows(BadRequestAlertException.class, () -> addressService.deleteAddress(addressId));
        assertEquals(expectedMessage, exception.getMessage());
        verify(addressRepository, never()).delete(existingAddress);
    }
}