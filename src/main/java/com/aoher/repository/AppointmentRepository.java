package com.aoher.repository;

import com.aoher.entities.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByEmployeeEmailContains(String email);
    List<Appointment> findByEmployeeId(Long id);
    List<Appointment> findByTimesLotBetween(LocalDateTime start, LocalDateTime end);
}
