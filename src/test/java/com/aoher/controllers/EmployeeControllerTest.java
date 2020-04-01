package com.aoher.controllers;

import com.aoher.controllers.abstracts.BaseControllerTest;
import com.aoher.entities.Employee;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.net.URL;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class EmployeeControllerTest extends BaseControllerTest {

    private static final String URN_EMPLOYEES = "/employees";

    @Test
    public void testAllEmployeesEmpty() throws Exception {
        String url = new URL(LOCAL_URL + port + URN_EMPLOYEES).toString();

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        assertEquals("[]", response.getBody());
    }

    @Test
    public void testAllEmployeesSuccess() throws Exception {
        seedEmployee();
        seedEmployee();
        seedEmployee();

        String url = new URL(LOCAL_URL + port + URN_EMPLOYEES).toString();

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        String responseBody = response.getBody();
        Employee[] createdEmployee = new ObjectMapper().readValue(Objects.requireNonNull(responseBody), Employee[].class);

        assertEquals(3, createdEmployee.length);
    }

    @Test
    public void testCreateEmployee() throws Exception {
        Employee employee = new Employee();
        employee.setName("John");
        employee.setEmail("john@gmail.com");

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Employee> request = new HttpEntity<>(employee, headers);

        String url = new URL(LOCAL_URL + port + URN_EMPLOYEES).toString();
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        String responseBody = response.getBody();
        Employee createdEmployee = new ObjectMapper().readValue(Objects.requireNonNull(responseBody), Employee.class);

        assertNotEquals(null, createdEmployee.getId());
    }

    @Test
    public void testEmployeeParamSuccess() throws Exception{
        Employee emp1 = seedEmployee();

        String url = new URL(LOCAL_URL + port + "/employees?email=" + emp1.getEmail()).toString();

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        String responseBody = response.getBody();
        Employee[] foundEmployee = new ObjectMapper().readValue(Objects.requireNonNull(responseBody), Employee[].class);

        assertEquals(emp1.getEmail(), foundEmployee[0].getEmail());
    }

    @Test
    public void testEmployeeParamFail() throws Exception{
        String url = new URL(LOCAL_URL + port + "/employees?email=").toString();

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        String responseBody = response.getBody();
        Employee[] foundEmployee = new ObjectMapper().readValue(Objects.requireNonNull(responseBody), Employee[].class);

        assertEquals(0, foundEmployee.length);
    }

}
