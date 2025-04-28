package com.example.equipmentmanagement.service;

import com.example.equipmentmanagement.dto.address.AddressDto;
import com.example.equipmentmanagement.dto.address.AddressSaveDto;
import com.example.equipmentmanagement.exception.BadRequestAlertException;
import com.example.equipmentmanagement.exception.ResourceNotFoundException;
import com.example.equipmentmanagement.mapper.AddressMapper;
import com.example.equipmentmanagement.model.Address;
import com.example.equipmentmanagement.repository.AddressRepository;
import com.example.equipmentmanagement.repository.EquipmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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

    private AddressSaveDto addressSaveDto;

    @BeforeEach
    void setUp() {
        addressSaveDto = new AddressSaveDto();
        addressSaveDto.setPostalCode("11-111");
        addressSaveDto.setCity("City");
        addressSaveDto.setStreet("Street");
        addressSaveDto.setNumber("11");
        addressSaveDto.setDescription("Description");
    }

    @Test
    @DisplayName("List of AddressDto objects returned")
    void testGetAllAddresses_returnsListOfAddressDto() {
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

    @Test
    @DisplayName("AddressDto object returned")
    void testGetAddress_whenAddressExists_returnsAddressDto() {
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

    @Test
    @DisplayName("ResourceNotFoundException thrown")
    void testGetAddress_whenAddressNotFound_throwsResourceNotFoundException() {
        // Given
        Long addressId = 1L;
        String expectedMessage = "Address with id 1 not found";
        when(addressRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> addressService.getAddress(addressId));
        assertEquals(expectedMessage, thrown.getMessage());
        verify(addressRepository, never()).save(any(Address.class));
    }

    @Test
    @DisplayName("Address object created")
    void testCreateAddress_whenAddressSaveDtoProvided_returnsAddressDto() {
        // Given
        Address address = new ModelMapper().map(addressSaveDto, Address.class);
        address.setId(1L);
        AddressDto addressDto = new ModelMapper().map(address, AddressDto.class);

        when(addressRepository.save(any(Address.class))).thenReturn(address);
        when(addressMapper.addressSaveDtoToAddress(any(AddressSaveDto.class))).thenReturn(address);
        when(addressMapper.addressToAddressDto(any(Address.class))).thenReturn(addressDto);

        // When
        AddressDto result = addressService.createAddress(addressSaveDto);

        // Then
        assertNotNull(result.getId());
        assertEquals(addressSaveDto.getPostalCode(), result.getPostalCode());
        assertEquals(addressSaveDto.getCity(), result.getCity());
        assertEquals(addressSaveDto.getStreet(), result.getStreet());
        assertEquals(addressSaveDto.getNumber(), result.getNumber());
        assertEquals(addressSaveDto.getDescription(), result.getDescription());
        verify(addressRepository).save(any(Address.class));
    }

    @Test
    @DisplayName("Address object updated")
    void testUpdateAddress_whenExists_updatesAndReturnsAddressDto() {
        // Given
        Long addressId = 1L;
        Address existingAddress = new Address();

        Address address = new ModelMapper().map(addressSaveDto, Address.class);
        address.setId(addressId);

        AddressDto addressDto = new ModelMapper().map(address, AddressDto.class);
        addressDto.setId(addressId);

        when(addressRepository.findById(anyLong())).thenReturn(Optional.of(existingAddress));
        when(addressMapper.addressSaveDtoToAddress(any(AddressSaveDto.class))).thenReturn(address);
        when(addressRepository.save(any(Address.class))).thenReturn(address);
        when(addressMapper.addressToAddressDto(any(Address.class))).thenReturn(addressDto);

        // When
        AddressDto result = addressService.updateAddress(addressId, addressSaveDto);

        // Then
        assertEquals(addressId, result.getId());
        assertEquals(addressSaveDto.getPostalCode(), result.getPostalCode());
        assertEquals(addressSaveDto.getCity(), result.getCity());
        assertEquals(addressSaveDto.getStreet(), result.getStreet());
        assertEquals(addressSaveDto.getNumber(), result.getNumber());
        assertEquals(addressSaveDto.getDescription(), result.getDescription());
        verify(addressRepository).save(address);
    }

    @Test
    @DisplayName("ResourceNotFoundException thrown")
    void testUpdateAddress_whenNotFound_throwsResourceNotFoundException() {
        // Given
        Long addressId = 1L;
        String expectedMessage = "Address with id 1 not found";

        when(addressRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> addressService.updateAddress(addressId, addressSaveDto));
        assertEquals(expectedMessage, thrown.getMessage());
        verify(addressRepository, never()).save(any(Address.class));
    }

    @Test
    @DisplayName("Address deleted")
    void testDeleteAddress_whenNoEquipmentLinked_deletesAddress() {
        // Given
        Address existingAddress = new Address();

        when(addressRepository.findById(anyLong())).thenReturn(Optional.of(existingAddress));
        when(equipmentRepository.countByAddressId(anyLong())).thenReturn(0);

        // When
        addressService.deleteAddress(1L);

        // Then
        verify(addressRepository).delete(existingAddress);
    }

    @Test
    @DisplayName("BadRequestAlertException thrown when there is Equipment linked to it")
    void testDeleteAddress_whenEquipmentLinked_throwsBadRequestAlertException() {
        // Given
        Address existingAddress = new Address();
        String expectedMessage = "Failed. There are equipment with this address: 1";

        when(addressRepository.findById(anyLong())).thenReturn(Optional.of(existingAddress));
        when(equipmentRepository.countByAddressId(anyLong())).thenReturn(1);

        // When & Then
        BadRequestAlertException thrown = assertThrows(BadRequestAlertException.class, () -> addressService.deleteAddress(1L));
        assertEquals(expectedMessage, thrown.getMessage());
        verify(addressRepository, never()).delete(existingAddress);
    }
}