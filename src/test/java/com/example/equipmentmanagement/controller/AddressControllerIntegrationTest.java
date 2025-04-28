package com.example.equipmentmanagement.controller;

import com.example.equipmentmanagement.dto.address.AddressDto;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "/application-test.properties")
class AddressControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("Address can be created")
    void testCreateAddress_whenValidAddress_returnsAddress() throws JSONException {
        // Given
        JSONObject addressRequestJson = new JSONObject();
        addressRequestJson.put("postalCode", "11-111");
        addressRequestJson.put("city", "City");
        addressRequestJson.put("street", "Street");
        addressRequestJson.put("number", "11");
        addressRequestJson.put("description", "Description");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<String> request = new HttpEntity<>(addressRequestJson.toString(), headers);

        // When
        ResponseEntity<AddressDto> response = restTemplate.postForEntity(
                "/api/address",
                request,
                AddressDto.class);

        AddressDto address = response.getBody();

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(address.getId());
        assertEquals("11-111", address.getPostalCode());
        assertEquals("City", address.getCity());
        assertEquals("Street", address.getStreet());
        assertEquals("11", address.getNumber());
        assertEquals("Description", address.getDescription());
    }

    @Test
    @DisplayName("GET /api/address requires JWT")
    void testGetAllAddresses_whenMissingJwt_returns401() {
        // Given
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");

        HttpEntity<String> request = new HttpEntity<>(null, headers);

        // When
        ResponseEntity<String> response = restTemplate.exchange(
                "/api/address",
                HttpMethod.GET,
                request,
                String.class);

        // Then
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}
