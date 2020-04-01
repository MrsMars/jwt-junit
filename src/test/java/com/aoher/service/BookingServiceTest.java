package com.aoher.service;

import com.aoher.entities.Appointment;
import com.aoher.entities.Employee;
import com.aoher.repository.AppointmentRepository;
import com.aoher.repository.EmployeeRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookingServiceTest {

    @Autowired
    private BookingService service;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;


    @Before
    public void setUp() {
        employee = new Employee();
        employee.setName("Ming Xiang");
        employee.setEmail("Chan");
        employeeRepository.save(employee);
    }

    @Test
    public void testCheckAppointment() {
        seedAppointment(employee);
        seedAppointment(employee);
        seedAppointment(employee);

        List<LocalDateTime> results = service.checkAppointment(employee);

        assertEquals(3, results.size());
    }

    @Test
    public void testBookAppointmentSuccess() {
        seedAppointment(employee);
        LocalDateTime intendedTime = LocalDateTime.now().plusHours(3);

        Appointment appointment = service.bookAppointment(intendedTime, employee);

        assertNotNull(appointment);
    }

    @Test
    public void testBookAppointmentFailureConflictBefore() {
        seedAppointment(employee);
        LocalDateTime intendedTime = LocalDateTime.now().plusHours(1);

        Appointment appointment = service.bookAppointment(intendedTime, employee);

        assertNull(appointment);
    }

    @Test
    public void testBookAppointmentFailureConflictAfter() {
        Appointment app = new Appointment();
        app.setTimeslot(LocalDateTime.now().plusHours(2));
        app.setEmployee(employee);

        appointmentRepository.save(app);
        LocalDateTime intendedTime = LocalDateTime.now().plusHours(1);

        Appointment appointment = service.bookAppointment(intendedTime, employee);

        assertNull(appointment);
    }

    private void seedAppointment(Employee employee) {
        Appointment app = new Appointment();

        app.setTimeslot(LocalDateTime.now());
        app.setEmployee(employee);

        appointmentRepository.save(app);
    }
}