package com.aoher.repository;

import com.aoher.entities.Appointment;
import com.aoher.entities.Employee;
import com.aoher.repository.abstractions.BaseRepositoryTest;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AppointmentRepositoryTest extends BaseRepositoryTest {

    @Test
    public void testFindAllEmpty() {
        List<Appointment> results = appointmentRepository.findAll();
        assertTrue(results.isEmpty());
    }

    @Test
    public void testFindAllSuccess() {
        seedAppointment(LocalDateTime.now());
        seedAppointment(LocalDateTime.now().plusHours(3));

        List<Appointment> results = appointmentRepository.findAll();
        assertEquals(2, results.size());
    }

    @Test
    public void testFindByTimesLotBetweenSuccess() {
        seedAppointment(LocalDateTime.now());
        Appointment target = seedAppointment((LocalDateTime.now().plusHours(3)));
        seedAppointment(LocalDateTime.now().plusHours(6));

        LocalDateTime start = LocalDateTime.now().plusHours(2);
        LocalDateTime end = LocalDateTime.now().plusHours(5);

        List<Appointment> results = appointmentRepository.findByTimesLotBetween(start, end);
        assertEquals(1, results.size());
        assertEquals(target.getId(), results.get(0).getId());
    }

    @Test
    public void testFindByTimesLotBetweenFailure() {
        seedAppointment(LocalDateTime.now());
        seedAppointment(LocalDateTime.now().plusHours(3));
        seedAppointment(LocalDateTime.now().plusHours(6));

        LocalDateTime start = LocalDateTime.now().plusHours(8);
        LocalDateTime end = LocalDateTime.now().plusHours(10);

        List<Appointment> results = appointmentRepository.findByTimesLotBetween(start, end);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testFindByEmployeeEmailContainsSuccess() {
        // Given
        Employee x = seedEmployee("Ming Xiang", "mingxiangchan@gmail.com");
        seedAppointment(LocalDateTime.now());
        Appointment y = seedAppointment(LocalDateTime.now());
        y.setEmployee(x);
        appointmentRepository.save(y);

        List<Appointment> results = appointmentRepository.findByEmployeeEmailContains("mingxiangchan");
        assertEquals(1, results.size());
    }
}