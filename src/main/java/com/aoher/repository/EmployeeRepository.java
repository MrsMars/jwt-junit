package com.aoher.repository;

import com.aoher.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Employee findByEmail(String email);
    List<Employee> findByName(String name);
    List<Employee> findAllByOrderByName();
    List<Employee> findByEmailContains(String email);
}