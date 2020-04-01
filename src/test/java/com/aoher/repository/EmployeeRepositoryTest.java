package com.aoher.repository;

import com.aoher.entities.Employee;
import com.aoher.repository.abstractions.BaseRepositoryTest;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class EmployeeRepositoryTest extends BaseRepositoryTest {

    @Test
    public void testFindAllEmpty() {
        List<Employee> results = employeeRepository.findAll();
        assertTrue(results.isEmpty());
    }

    @Test
    public void testFindAllSuccess() {

        seedEmployee("Ming Xiang", "mingxiangchan@gmail.com");
        seedEmployee("Wan Jun", "wanjuntham@gmail.com");

        List<Employee> results = employeeRepository.findAll();
        assertEquals(2, results.size());
    }

    @Test
    public void testFindIdSuccess() {
        Employee emp1 = seedEmployee("test1", "test1@test.com");
        seedEmployee("test2", "test2@test.com");

        Employee results = employeeRepository.findById(emp1.getId()).orElse(null);

        assertNotNull(results);
        assertEquals(emp1.getId(), results.getId());
    }

    @Test
    public void testFindIdFail() {
        Employee result = employeeRepository.findById(5L).orElse(null);
        assertNull(result);
    }

    @Test
    public void testFindEmailSuccess() {
        Employee emp1 = seedEmployee("Ming Xiang", "mingxiangchan@gmail.com");
        seedEmployee("Wan Jun", "wanjuntham@gmail.com");

        Employee results = employeeRepository.findByEmail(emp1.getEmail());
        assertEquals(emp1.getId(), results.getId());
    }

    @Test
    public void testFindNameSuccess() {
        Employee emp1 = seedEmployee("Wan Jun", "wanjuntham@gmail.com");

        List<Employee> results = employeeRepository.findByName(emp1.getName());
        assertEquals(emp1.getName(), results.get(emp1.getId().intValue()).getName());
    }

    @Test

    public void findAllWithSorting() {
        Employee emp1 = seedEmployee("a", "a@gmail.com");
        Employee emp2 = seedEmployee("b", "b@gmail.com");
        Employee emp3 = seedEmployee("c", "c@gmail.com");

        List<Employee> results = employeeRepository.findAllByOrderByName();

        assertEquals(emp1.getName(),results.get(0).getName());
        assertEquals(emp2.getName(),results.get(1).getName());
        assertEquals(emp3.getName(),results.get(2).getName());
    }
}