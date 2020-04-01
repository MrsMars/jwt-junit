package com.aoher.controllers;

import com.aoher.entities.Appointment;
import com.aoher.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AppointmentController {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @GetMapping("/appointments")
    public List<Appointment> allAppointments() {
        return appointmentRepository.findAll();
    }

    @GetMapping("/appointments/{id}")
    public Appointment appointmentById(@PathVariable Long id) {
        return appointmentRepository.findById(id).orElse(null);
    }

    @GetMapping("/employees/{id}/appointments")
    public List<Appointment> appointmentsByEmployee(@PathVariable Long id) {
        return appointmentRepository.findByEmployeeId(id);
    }

    @PostMapping("/appointments")
    public Appointment createAppointment(@RequestBody Appointment appointment,
                                         @RequestParam(required = false) Long employeeId) {
        appointmentRepository.save(appointment);
        return appointment;
    }
}
