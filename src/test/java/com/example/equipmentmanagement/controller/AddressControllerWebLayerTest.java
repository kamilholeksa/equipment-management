package com.example.equipmentmanagement.controller;

import com.example.equipmentmanagement.dto.address.AddressDto;
import com.example.equipmentmanagement.dto.address.AddressSaveDto;
import com.example.equipmentmanagement.repository.UserRepository;
import com.example.equipmentmanagement.security.jwt.TokenProvider;
import com.example.equipmentmanagement.service.AddressService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AddressController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class AddressControllerWebLayerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    TokenProvider tokenProvider;
    @MockitoBean
    UserRepository userRepository;

    @MockitoBean
    AddressService addressService;

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
    @DisplayName("Address can be created")
    void testCreateAddress_whenValidAddressProvided_returnsAddressDto() throws Exception {
        // Given
        AddressDto addressDto = new ModelMapper().map(addressSaveDto, AddressDto.class);
        addressDto.setId(1L);

        when(addressService.createAddress(any(AddressSaveDto.class))).thenReturn(addressDto);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/address")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(addressSaveDto));

        // When
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        String responseBodyAsString = mvcResult.getResponse().getContentAsString();
        AddressDto createdAddress = new ObjectMapper().readValue(responseBodyAsString, AddressDto.class);

        // Then
        assertNotNull(createdAddress.getId());
        assertEquals(addressSaveDto.getPostalCode(), createdAddress.getPostalCode());
        assertEquals(addressSaveDto.getCity(), createdAddress.getCity());
        assertEquals(addressSaveDto.getStreet(), createdAddress.getStreet());
        assertEquals(addressSaveDto.getNumber(), createdAddress.getNumber());
        assertEquals(addressSaveDto.getDescription(), createdAddress.getDescription());
    }

    @Test
    @DisplayName("Postal code cannot be blank")
    void testCreateAddress_whenPostalCodeIsBlank_returns400StatusCode() throws Exception {
        // Given
        addressSaveDto.setPostalCode("");

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(addressSaveDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("City cannot be blank")
    void testCreateAddress_whenCityIsBlank_returns400StatusCode() throws Exception {
        // Given
        addressSaveDto.setCity("");

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(addressSaveDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Street cannot be blank")
    void testCreateAddress_whenStreetIsBlank_returns400StatusCode() throws Exception {
        // Given
        addressSaveDto.setStreet("");

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(addressSaveDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Number cannot be blank")
    void testCreateAddress_whenNumberIsBlank_returns400StatusCode() throws Exception {
        // Given
        addressSaveDto.setNumber("");

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(addressSaveDto)))
                .andExpect(status().isBadRequest());
    }
}
