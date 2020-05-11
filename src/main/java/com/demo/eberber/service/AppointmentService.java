package com.demo.eberber.service;

import com.demo.eberber.domain.Appointment;
import com.demo.eberber.Dto.AppointmentDto.*;
import com.demo.eberber.exception.BadResourceException;
import com.demo.eberber.exception.ResourceAlreadyExistsException;
import com.demo.eberber.exception.ResourceNotFoundException;
import com.demo.eberber.repository.AppointmentRepository;
import com.demo.eberber.specification.AppointmentSpecification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/*import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;*/

@Service
public class AppointmentService {
    @Autowired
    private AppointmentRepository appointmentRepository;

    private boolean existById(Long i) { return appointmentRepository.existsById(i);}

    public Appointment findById(long id) throws ResourceNotFoundException {
        Appointment appointment = appointmentRepository.findById(id).orElse(null);
        if(appointment == null)
            throw new ResourceNotFoundException("Cannot find Appointment with id :" +id);
        else return appointment;
    }
    //randevuları listelemek
    public List<Appointment> findAll(int pageNumber, int rowPerPage) throws  ResourceNotFoundException {
        List<Appointment> appointments = new ArrayList<>();
        appointmentRepository.findAll(PageRequest.of(pageNumber-1,rowPerPage)).forEach(appointments::add);
        if(appointments == null)
            throw new ResourceNotFoundException("No appointments were found.\n");
        else
            return appointments;
    }
    //barber id e gore listelemek
    public List<Appointment> findAllByBarberId(long id) throws ResourceNotFoundException{
        Appointment filter = new Appointment();
        filter.setBarberId((int)id);
        Specification<Appointment> spec = new AppointmentSpecification(filter);

        List<Appointment> appointmentsBarber = new ArrayList<>();
        Iterable<Appointment> i = appointmentRepository.findByBarberId(id);
        i.forEach(appointmentsBarber::add);
        if(appointmentsBarber == null)
            throw  new ResourceNotFoundException("Cannot find Appointment with barber id:" + id);
        else
            return appointmentsBarber;
    }

    public List<Appointment> findAllByCustomerId(long id) throws ResourceNotFoundException{
        Appointment filter = new Appointment();
        filter.setCustomerId((int)id);
        Specification<Appointment> spec = new AppointmentSpecification(filter);

        List<Appointment> appointmentsCustomer = new ArrayList<>();
        Iterable<Appointment> i = appointmentRepository.findByCustomerId(id);
        i.forEach(appointmentsCustomer::add);
        if(appointmentsCustomer == null)
            throw new ResourceNotFoundException("Cannot find Appointments with customer id:" + id);
        else
            return appointmentsCustomer;
    }
    public List<Appointment> findAllByStaffId(long id) throws ResourceNotFoundException{
        Appointment filter = new Appointment();
        filter.setCustomerId((int)id);
        Specification<Appointment> spec = new AppointmentSpecification(filter);

        List<Appointment> appointmentsStaff = new ArrayList<>();
        Iterable<Appointment> i = appointmentRepository.findByStaffId(id);
        i.forEach(appointmentsStaff::add);
        if(appointmentsStaff == null)
            throw new ResourceNotFoundException("Cannot find Appointments with customer id:" + id);
        else
            return appointmentsStaff;
    }
    public List<Appointment> findAllByDate(Date appointmentDate) throws ResourceNotFoundException{
        Appointment filter = new Appointment();
        filter.setAppointmentDate(appointmentDate);
        Specification<Appointment> spec = new AppointmentSpecification(filter);

        List<Appointment> appointmentsDate = new ArrayList<>();
        Iterable<Appointment> i = appointmentRepository.findByAppointmentDate(appointmentDate);
        i.forEach(appointmentsDate::add);
        if(appointmentsDate == null)
            throw new ResourceNotFoundException("Cannot find Appointments with Date:" + appointmentDate);
        else
            return appointmentsDate;
    }


    //berber id ile tarihe göre
    public List<Appointment> findDateandBarberId (Date appointmentDate, long barberId) throws ResourceNotFoundException {
        List<Appointment> appointmentsBarber = new ArrayList<>();
        Iterable<Appointment> i = appointmentRepository.findByAppointmentDateAndBarberId(appointmentDate, barberId);//findByBarber(appointmentsFilter.date, appointmentsFilter.barberId);
        i.forEach(appointmentsBarber::add);
        if(appointmentsBarber == null)
            throw  new ResourceNotFoundException("Cannot find Appointment with barber id:" + (int)barberId + " Date:" + appointmentDate);
        else
            return appointmentsBarber;
    }

    public List<Appointment> findDateandCustomerId (Date appointmentDate, long customerId) throws ResourceNotFoundException {
        List<Appointment> appointmentsBarber = new ArrayList<>();
        Iterable<Appointment> i = appointmentRepository.findByAppointmentDateAndCustomerId(appointmentDate, customerId);
        i.forEach(appointmentsBarber::add);
        if(appointmentsBarber == null)
            throw  new ResourceNotFoundException("Cannot find Appointment with barber id:" + customerId + " Date:" + appointmentDate);
        else
            return appointmentsBarber;
    }

    public List<Appointment> findDateandStaffId (Date appointmentDate, long staffId) throws ResourceNotFoundException {
        List<Appointment> appointmentsBarber = new ArrayList<>();
        Iterable<Appointment> i = appointmentRepository.findByAppointmentDateAndStaffId(appointmentDate, staffId);
        i.forEach(appointmentsBarber::add);
        if(appointmentsBarber == null)
            throw  new ResourceNotFoundException("Cannot find Appointment with barber id:" + staffId + " Date:" + appointmentDate);
        else
            return appointmentsBarber;
    }

    public List<Appointment> filterDateBetween(Date startDate, Date endDate) throws ResourceNotFoundException {
        List<Appointment> appointmentsFilter = new ArrayList<>();
        Iterable<Appointment> i = appointmentRepository.findByAppointmentDateBetween(startDate, endDate);
        i.forEach(appointmentsFilter::add);
        if(appointmentsFilter == null)
            throw  new ResourceNotFoundException("No appointment was found between two dates\n");
        else
            return appointmentsFilter;
    }

    public List<Appointment> findByCustomerDateBefore(long customerId, Date date) throws ResourceNotFoundException {
        List<Appointment> appointmentsFilter = new ArrayList<>();
        Iterable<Appointment> i = appointmentRepository.findByCustomerIdAndAppointmentDateBefore(customerId, date);
        i.forEach(appointmentsFilter::add);
        if(appointmentsFilter == null)
            throw  new ResourceNotFoundException("No appointment was found between the two dates of the employee.\n");
        else
            return appointmentsFilter;
    }

    public List<Appointment> findByBarberDateBefore(long barberId, Date date) throws ResourceNotFoundException {
        List<Appointment> appointmentsFilter = new ArrayList<>();
        Iterable<Appointment> i = appointmentRepository.findByBarberIdAndAppointmentDateBefore(barberId, date);
        i.forEach(appointmentsFilter::add);
        if(appointmentsFilter == null)
            throw  new ResourceNotFoundException("No appointment was found between the two dates of the employee.\n");
        else
            return appointmentsFilter;
    }

    public List<Appointment> findByStaffDateBefore(long staffId, Date date) throws ResourceNotFoundException {
        List<Appointment> appointmentsFilter = new ArrayList<>();
        Iterable<Appointment> i = appointmentRepository.findByStaffIdAndAppointmentDateBefore(staffId, date);
        i.forEach(appointmentsFilter::add);
        if(appointmentsFilter == null)
            throw  new ResourceNotFoundException("No appointment was found between the two dates of the employee.\n");
        else
            return appointmentsFilter;
    }

    public List<Appointment> filterDateBetweenByStaffId(long staffId, Date startDate, Date endDate) throws ResourceNotFoundException {
        List<Appointment> appointmentsFilter = new ArrayList<>();
        Iterable<Appointment> i = appointmentRepository.findByStaffIdAndAppointmentDateBetween(staffId, startDate, endDate);
        i.forEach(appointmentsFilter::add);
        if(appointmentsFilter == null)
            throw  new ResourceNotFoundException("No appointment was found between the two dates of the employee.\n");
        else
            return appointmentsFilter;
    }

    public List<Appointment> filterDateBetweenByBarberId(long barberId, Date startDate, Date endDate) throws ResourceNotFoundException {
        List<Appointment> appointmentsFilter = new ArrayList<>();
        Iterable<Appointment> i = appointmentRepository.findByBarberIdAndAppointmentDateBetween(barberId, startDate, endDate);
        i.forEach(appointmentsFilter::add);
        if(appointmentsFilter == null)
            throw  new ResourceNotFoundException("No appointment was found between the two dates of the barber.\n");
        else
            return appointmentsFilter;
    }

    public List<Appointment> filterDateBetweenByCustomerId(long customerId, Date startDate, Date endDate) throws ResourceNotFoundException {
        List<Appointment> appointmentsFilter = new ArrayList<>();
        Iterable<Appointment> i = appointmentRepository.findByCustomerIdAndAppointmentDateBetween(customerId, startDate, endDate);
        i.forEach(appointmentsFilter::add);
        if(appointmentsFilter == null)
            throw  new ResourceNotFoundException("No appointment was found between the two dates of the customer.\n");
        else
            return appointmentsFilter;
    }

    public List<Appointment> findByStaffIdMonthly(long staffId) throws ResourceNotFoundException {
        List<Appointment> appointmentsFilter = new ArrayList<>();
        Date date = new Date();
        Date lastMonth = DateUtils.addMonths(date, -1);
        Iterable<Appointment> i = appointmentRepository.findByStaffIdAndAppointmentDateAfter(staffId, lastMonth);
        i.forEach(appointmentsFilter::add);
        if(appointmentsFilter == null)
            throw  new ResourceNotFoundException("No appointment was found after the dates of the employee.\n");
        else
            return appointmentsFilter;
    }

    public List<Appointment> findByBarberIdMonthly(long barberId) throws ResourceNotFoundException {
        List<Appointment> appointmentsFilter = new ArrayList<>();
        Date date = new Date();
        Date lastMonth = DateUtils.addMonths(date, -1);
        Iterable<Appointment> i = appointmentRepository.findByBarberIdAndAppointmentDateAfter(barberId, lastMonth);
        i.forEach(appointmentsFilter::add);
        if(appointmentsFilter == null)
            throw  new ResourceNotFoundException("No appointment was found monthly the dates of the barber.\n");
        else
            return appointmentsFilter;
    }

    public List<Appointment> findByCustomerIdMonthly(long customerId) throws ResourceNotFoundException {
        List<Appointment> appointmentsFilter = new ArrayList<>();
        Date date = new Date();
        Date lastMonth = DateUtils.addMonths(date, -1);
        Iterable<Appointment> i = appointmentRepository.findByCustomerIdAndAppointmentDateAfter(customerId, lastMonth);
        i.forEach(appointmentsFilter::add);
        if(appointmentsFilter == null)
            throw  new ResourceNotFoundException("No appointment was found after the dates of the customer.\n");
        else
            return appointmentsFilter;
    }


    //Randevu ekleme
    public Appointment save(Appointment appointment) throws BadResourceException, ResourceNotFoundException,ResourceAlreadyExistsException {
        if(!StringUtils.isEmpty(appointment.getAppointmentDate())){
            if(appointment.getId() != 0 && existById(appointment.getId()))
                throw  new ResourceNotFoundException("Appointment with id " + appointment.getId() + "already exists" );
            return  appointmentRepository.save(appointment);
        }
        else {
            BadResourceException exc = new BadResourceException("Failed to save appointment");
            exc.addErrorMessage("Appointment is null or empty");
            throw exc;
        }
    }
    //Randevu tarih guncelleme
    public Appointment update(Appointment appointment) throws ResourceNotFoundException, BadResourceException {
        if(!existById(appointment.getBarberId())) {
            if(!existById( appointment.getId()))
                throw new ResourceNotFoundException("Appointment find Contact with id: " + appointment.getId());
            return appointmentRepository.save(appointment);
        }
        else {
            BadResourceException exc = new BadResourceException("Failed to save appointment");
            exc.addErrorMessage("Appointment is null or empty");
            throw exc;
        }
    }
    //Idye gore silme
    public void deleteById(Long id) throws ResourceNotFoundException  {
        if(!existById( id)) {
            throw new ResourceNotFoundException("Cannot find appointment with id: " + id);
        }
        else {
            appointmentRepository.deleteById(id);
            throw new ResourceNotFoundException("Delete appointment with id: " + id);
        }
    }
    //Kac adet randevu var
    public  Long count() {
        return appointmentRepository.count();
    }


}
