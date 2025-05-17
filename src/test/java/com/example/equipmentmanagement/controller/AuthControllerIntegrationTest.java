package com.example.equipmentmanagement.controller;

import com.example.equipmentmanagement.dto.auth.AuthResponse;
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
class AuthControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("Login works")
    void testLogin_whenValidCredentialsProvided_returnsAuthResponse() throws JSONException {
        // Given
        JSONObject credentialsJson = new JSONObject();
        credentialsJson.put("username", "admin");
        credentialsJson.put("password", "adminadmin");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<String> request = new HttpEntity<>(credentialsJson.toString(), headers);

        // When
        ResponseEntity<AuthResponse> response = restTemplate.postForEntity(
                "/login",
                request,
                AuthResponse.class);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getAccessToken());
        assertNotNull(response.getBody().getRefreshToken());
    }
}
