package com.aoher.controllers.abstracts;

import com.aoher.entities.Appointment;
import com.aoher.entities.Employee;
import com.aoher.repository.AppointmentRepository;
import com.aoher.repository.EmployeeRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseControllerTest {

    protected static final String LOCAL_URL = "http://localhost:";

    @LocalServerPort
    protected int port;

    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    protected AppointmentRepository appointmentRepository;

    @Autowired
    protected EmployeeRepository employeeRepository;

    @Before
    public void cleanDBBefore() {
        appointmentRepository.deleteAll();
        employeeRepository.deleteAll();
    }

    @After
    public void cleanDBAfter() {
        appointmentRepository.deleteAll();
        employeeRepository.deleteAll();
    }

    protected Employee seedEmployee() {
        Employee employee = new Employee();

        employee.setName("Test");
        employee.setEmail("test@test.com");
        employee.setPassword("testtest");

        employeeRepository.save(employee);

        return employee;
    }

    protected Appointment seedAppointment(LocalDateTime time) {
        Appointment appointment = new Appointment();

        appointment.setTimeslot(time);
        appointmentRepository.save(appointment);

        return appointment;
    }

    protected String seedSignUpEmployeeToken(Employee employee) throws MalformedURLException, JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Employee> request = new HttpEntity<>(employee, headers);


        String url = new URL(LOCAL_URL + port + "/signup").toString();

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        String responseBody = response.getBody();

        Map<String,String> newEmployeeToken = new ObjectMapper().readValue(Objects.requireNonNull(responseBody), getGenericMapType());

        return newEmployeeToken.get("token");
    }

    protected String seedLoginEmployeeToken(Employee employee) throws MalformedURLException {

        HttpHeaders loginHeaders = new HttpHeaders();
        HttpEntity<Employee> loginRequest = new HttpEntity<>(employee, loginHeaders);

        String loginUrl = new URL(LOCAL_URL + port + "/login").toString();
        ResponseEntity<String> loginResponse = restTemplate.postForEntity(loginUrl, loginRequest, String.class);

        return Objects.requireNonNull(loginResponse.getHeaders().get("Authorization")).get(0).replace("Bearer ", "");
    }

    protected static JavaType getGenericMapType() {
        ObjectMapper mapper = new ObjectMapper();
        JavaType stringType = mapper.getTypeFactory().constructType(String.class);
        return mapper.getTypeFactory().constructMapType(Map.class, stringType, stringType);
    }
}
