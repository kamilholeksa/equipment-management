package com.example.equipmentmanagement.controller;

import com.example.equipmentmanagement.dto.auth.AuthResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IntegrationTestUtils {

    @Autowired
    private TestRestTemplate restTemplate;

    public String obtainAccessToken() throws JSONException {
        JSONObject credentialsJson = new JSONObject();
        credentialsJson.put("username", "admin");
        credentialsJson.put("password", "admin");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<String> request = new HttpEntity<>(credentialsJson.toString(), headers);

        ResponseEntity<AuthResponse> response = restTemplate.postForEntity(
                "/login",
                request,
                AuthResponse.class);

        return response.getBody().getAccessToken();
    }
}
