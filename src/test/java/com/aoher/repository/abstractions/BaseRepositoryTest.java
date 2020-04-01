package com.aoher.repository.abstractions;

import com.aoher.entities.Appointment;
import com.aoher.entities.Employee;
import com.aoher.repository.AppointmentRepository;
import com.aoher.repository.EmployeeRepository;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public abstract class BaseRepositoryTest {

    @Autowired
    protected AppointmentRepository appointmentRepository;

    @Autowired
    protected EmployeeRepository employeeRepository;

    protected Employee seedEmployee(String name, String email) {
        Employee employee = new Employee();
        employee.setName(name);
        employee.setEmail(email);
        employeeRepository.save(employee);
        return employee;
    }

    protected Appointment seedAppointment(LocalDateTime time) {
        Appointment appointment = new Appointment();
        appointment.setTimeslot(time);
        appointmentRepository.save(appointment);
        return appointment;
    }
}
