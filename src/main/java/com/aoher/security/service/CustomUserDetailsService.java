package com.aoher.security.service;

import com.aoher.entities.Employee;
import com.aoher.repository.EmployeeRepository;
import com.aoher.security.model.CustomUserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private EmployeeRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        Employee employee = repository.findByEmail(email);

        if (employee == null) {
            throw new UsernameNotFoundException(email);
        }
        return new CustomUserPrincipal(employee);
    }

    public Employee getCurrentEmployee() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Employee currentEmployee = null;

        if (principal instanceof String) {
            String employeeId = (String) principal;
            currentEmployee = repository.findById(Long.parseLong(employeeId)).orElse(null);
        } else if (principal instanceof CustomUserPrincipal) {
            CustomUserPrincipal customPrincipal = (CustomUserPrincipal) principal;
            currentEmployee = customPrincipal.getEmployee();
        }

        return currentEmployee;
    }
}
