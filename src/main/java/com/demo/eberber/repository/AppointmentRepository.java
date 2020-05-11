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
    List<Appointment> findByAppointmentDateBetween(Date startDate, Date endDate);
    List<Appointment> findByBarberIdAndAppointmentDateBetween(long barberId, Date startDate, Date endDate);
    List<Appointment> findByStaffIdAndAppointmentDateBetween(long staffId, Date startDate, Date endDate);
    List<Appointment> findByCustomerIdAndAppointmentDateBetween(long customerId, Date startDate, Date endDate);
    List<Appointment> findByCustomerIdAndAppointmentDateBefore(long customerId, Date AppointmentDate);
    List<Appointment> findByBarberIdAndAppointmentDateBefore(long barberId, Date appointmentDate);
    List<Appointment> findByStaffIdAndAppointmentDateBefore(long staffId, Date appointmentDate);
    List<Appointment> findByBarberIdAndAppointmentDateAfter(long staffId, Date appointmentDate);
    List<Appointment> findByStaffIdAndAppointmentDateAfter(long staffId, Date appointmentDate);
    List<Appointment> findByCustomerIdAndAppointmentDateAfter(long customerId, Date appointmentDate);
}
