package com.aoher.controllers;

import com.aoher.controllers.abstracts.BaseControllerTest;
import com.aoher.entities.Appointment;
import com.aoher.entities.Employee;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.Objects;

import static org.junit.Assert.*;

public class AppointmentControllerTest extends BaseControllerTest {

    private static final String URN_APPOINTMENT = "/appointments";

    @Test
    public void testAllAppointmentsEmpty() throws Exception {
        String url = new URL(LOCAL_URL + port + URN_APPOINTMENT).toString();

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        assertEquals("[]", response.getBody());
    }

    @Test
    public void testAllAppointmentsSuccess() throws Exception {
        seedAppointment(LocalDateTime.now());
        seedAppointment(LocalDateTime.now());
        seedAppointment(LocalDateTime.now());
        seedAppointment(LocalDateTime.now());

        String url = new URL(LOCAL_URL + port + URN_APPOINTMENT).toString();

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        String responseBody = response.getBody();
        Appointment[] createdAppointments = new ObjectMapper().readValue(Objects.requireNonNull(responseBody), Appointment[].class);

        assertEquals(4, createdAppointments.length);
    }

    @Test
    public void testAppointmentsByEmployee() throws Exception {
        Appointment app1 = seedAppointment(LocalDateTime.now());
        Employee emp1 = seedEmployee();
        app1.setEmployee(emp1);
        appointmentRepository.save(app1);

        seedAppointment(LocalDateTime.now());
        seedAppointment(LocalDateTime.now());

        String url = new URL(LOCAL_URL + port + "/employees/" + emp1.getId() + URN_APPOINTMENT).toString();

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        String responseBody = response.getBody();
        Appointment[] appointments = new ObjectMapper().readValue(Objects.requireNonNull(responseBody), Appointment[].class);

        assertEquals(1, appointments.length);
        assertEquals(app1.getId(), appointments[0].getId());
    }

    @Test
    public void testAppointmentsById() throws Exception{
        Appointment app1 = seedAppointment(LocalDateTime.now());

        // set up the route and url
        String url = new URL(LOCAL_URL + port + "/appointments/" + app1.getId()).toString();

        // Send the HTTP request
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        // converting the JSON string into Employee objects
        String responseBody = response.getBody();
        Appointment foundAppointment = new ObjectMapper().readValue(Objects.requireNonNull(responseBody), Appointment.class);

        assertEquals(app1.getId(), foundAppointment.getId());
    }

    @Test
    public void testCreateAppointment() throws Exception {
        Appointment app1 = seedAppointment(LocalDateTime.now());
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Appointment> request = new HttpEntity<>(app1,headers);

        String url = new URL(LOCAL_URL + port + URN_APPOINTMENT).toString();

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        String responseBody = response.getBody();
        Appointment createdAppointment = new ObjectMapper().readValue(Objects.requireNonNull(responseBody), Appointment.class);

        assertNotEquals(null, createdAppointment.getId());
    }

    @Test
    public void appointmentsByEmployeeTestWithSecurity() throws Exception {
        Employee emp1 = seedEmployee();
        Appointment app1 = seedAppointment(LocalDateTime.now());
        Appointment app2 = seedAppointment(LocalDateTime.now());

        app1.setEmployee(emp1);
        app2.setEmployee(emp1);

        appointmentRepository.save(app1);
        appointmentRepository.save(app2);

        String emp1Token = seedSignUpEmployeeToken(emp1);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer ".concat(emp1Token));

        HttpEntity<Appointment> request = new HttpEntity<>(null, headers);
        String url = new URL(LOCAL_URL + port + "/employees/" + emp1.getId() + URN_APPOINTMENT).toString();

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
        String responseBody = response.getBody();

        Appointment[] appointments = new ObjectMapper().readValue(Objects.requireNonNull(responseBody), Appointment[].class);

        assertEquals(2, appointments.length);
    }

    @Test
    public void testCreateAppointmentWithUser() throws Exception {
        Employee emp1 = seedEmployee();
        seedSignUpEmployeeToken(emp1);

        String emp1Token = seedLoginEmployeeToken(emp1);

        assertNotNull(emp1Token);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer ".concat(emp1Token));

        Appointment app1 = seedAppointment(LocalDateTime.now());
        app1.setEmployee(emp1);

        HttpEntity<Appointment> request = new HttpEntity<>(app1,headers);
        String url = new URL(LOCAL_URL + port + URN_APPOINTMENT).toString();

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        String responseBody = response.getBody();
        Appointment createdAppointment = new ObjectMapper().readValue(Objects.requireNonNull(responseBody), Appointment.class);

        assertNotEquals(null, createdAppointment.getId());

    }
}