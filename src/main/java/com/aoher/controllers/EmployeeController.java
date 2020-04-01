package com.aoher.controllers;

import com.aoher.entities.Employee;
import com.aoher.repository.EmployeeRepository;
import com.aoher.security.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping("/employees")
    public List<Employee> allEmployees(@RequestParam(required = false) String email) {
        return email != null ? employeeRepository.findByEmailContains(email) : employeeRepository.findAll();
    }

    @PostMapping("/employees")
    public Employee createEmployee(@RequestBody Employee employee) {
        employeeRepository.save(employee);
        return employee;
    }

    @GetMapping("/employee/{id}")
    public Employee employeeById(@PathVariable Long id) {
        return employeeRepository.findById(id).orElse(null);
    }

    @PostMapping("/signup")
    public Map<String, String> signup(@RequestBody Employee employee) {
        employeeRepository.save(employee);

        String token = JwtUtils.generateJwt(employee);
        Map<String, String> body = new HashMap<>();
        body.put("token", token);

        return body;
    }
}
