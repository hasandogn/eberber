package com.demo.eberber.repository;

import com.demo.eberber.domain.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Date;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long>{
    List<Appointment> findByBarberId(long barberId);
    List<Appointment> findByAppointmentDate(Date appointmentDate);
    List<Appointment> findByCustomerId(long id);
    List<Appointment> findByStaffId(long id);
    List<Appointment> findByAppointmentDateAndBarberId(Date appointmentDate, long id);
    List<Appointment> findByAppointmentDateAndCustomerId(Date appointmentDate, long id);
    List<Appointment> findByAppointmentDateAndStaffId(Date appointmentDate, long id);
}
