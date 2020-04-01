package com.aoher.controllers;

import com.aoher.controllers.abstracts.BaseControllerTest;
import com.aoher.entities.Employee;
import com.aoher.security.util.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.net.URL;
import java.util.Map;
import java.util.Objects;

import static org.junit.Assert.assertNotNull;

public class AuthTest extends BaseControllerTest {

    @Test
    public void signUpTest() throws Exception {
        Employee emp1 = seedEmployee();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Employee> request = new HttpEntity<>(emp1, headers);

        String url = new URL(LOCAL_URL + port + "/signup").toString();

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        String responseBody = response.getBody();
        Map<String,String> newEmployeeToken = new ObjectMapper().readValue(Objects.requireNonNull(responseBody), getGenericMapType());

        assertNotNull(JwtUtils.parseJwt(newEmployeeToken.get("token")));
    }

    @Test
    public void loginTest() throws Exception {
        Employee emp1 = seedEmployee();
        seedSignUpEmployeeToken(emp1);

        HttpHeaders loginHeaders = new HttpHeaders();
        HttpEntity<Employee> loginRequest = new HttpEntity<>(emp1, loginHeaders);

        String loginUrl = new URL(LOCAL_URL + port + "/login").toString();
        ResponseEntity<String> loginResponse = restTemplate.postForEntity(loginUrl, loginRequest, String.class);

        String loginResponseToken = Objects.requireNonNull(loginResponse.getHeaders().get("Authorization")).get(0)
                .replace("Bearer ", "");

        assertNotNull(loginResponseToken);
    }
}
