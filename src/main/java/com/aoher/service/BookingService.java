package com.aoher.service;

import com.aoher.entities.Appointment;
import com.aoher.entities.Employee;
import com.aoher.repository.AppointmentRepository;
import com.aoher.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<LocalDateTime> checkAppointment(Employee employee) {
        List<Appointment> appointments = appointmentRepository.findByEmployeeEmailContains(employee.getEmail());

        return appointments.stream().map(Appointment::getTimeslot)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public Appointment bookAppointment(LocalDateTime timesLot, Employee employee) {
        if (ableToBook(timesLot)) {
            Appointment appointment = new Appointment();
            appointment.setTimeslot(timesLot);
            appointment.setEmployee(employee);
            appointmentRepository.save(appointment);
            return appointment;
        }
        return null;
    }

    public boolean ableToBook(LocalDateTime timesLot) {
        LocalDateTime start = timesLot.minusHours(2);
        LocalDateTime end = timesLot.plusHours(2);

        return appointmentRepository.findByTimesLotBetween(start, end).isEmpty();
    }
}
