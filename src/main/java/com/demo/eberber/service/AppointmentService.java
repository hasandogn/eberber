package com.demo.eberber.service;

import com.demo.eberber.domain.Appointment;
import com.demo.eberber.exception.BadResourceException;
import com.demo.eberber.exception.ResourceAlreadyExistsException;
import com.demo.eberber.exception.ResourceNotFoundException;
import com.demo.eberber.repository.AppointmentRepository;
import com.demo.eberber.specification.AppointmentSpecification;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class AppointmentService {
    @Autowired
    private AppointmentRepository appointmentRepository;

    private boolean existById(Long i) { return appointmentRepository.existsById(i);}

    public Appointment findById(Long id) throws ResourceNotFoundException {
        Appointment appointment = appointmentRepository.findById(id).orElse(null);
        if(appointment == null) {
            throw new ResourceNotFoundException("Cannot find Appointment with id :" +id);
        }
        else return appointment;
    }
    //randevuları listelemek
    public List<Appointment> findAll(int pageNumber, int rowPerPage) {
        List<Appointment> appointments = new ArrayList<>();
        appointmentRepository.findAll(PageRequest.of(pageNumber-1,rowPerPage)).forEach(appointments::add);
        return appointments;
    }
    //barber id e gore listelemek
    public List<Appointment> findAllByBarberId(int id){
        Appointment filter = new Appointment();
        filter.setBarberId(id);
        Specification<Appointment> spec = new AppointmentSpecification(filter);

        List<Appointment> appointments = new ArrayList<>();
        Iterable<Appointment> i = appointmentRepository.findAll();
        i.forEach(appointments::add);
        return appointments;
    }
    //berber id ile tarihe göre
    /*public List<Appointment> findDateandBarberId (Long barberId, Long serviceId) {
        Appointment filter = new Appointment();
        filter.setBarberId(barberId);
        filter.setServiceId(serviceId);

    }*/

    //Randevu ekleme
    public Appointment save(Appointment appointment) throws BadResourceException, ResourceNotFoundException,ResourceAlreadyExistsException {
        if(!StringUtils.isEmpty(appointment.getAppointDate())){
            if(appointment.getId() != 0 && existById((long) appointment.getId())){
                throw  new ResourceNotFoundException("Appointment with id " + appointment.getId() + "already exists" );
            }
            return  appointmentRepository.save(appointment);
        }
        else {
            BadResourceException exc = new BadResourceException("Failed to save appointment");
            exc.addErrorMessage("Appointment is null or empty");
            throw exc;
        }
    }
    //Randevu tarih guncelleme
    public void update(Appointment appointment) throws ResourceNotFoundException, BadResourceException {
        if(!existById((long) appointment.getBarberId())) {
            if(!existById((long) appointment.getId())) {
                throw new ResourceNotFoundException("Appointment find Contact with id: " + appointment.getId());
            }
            appointmentRepository.save(appointment);
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
        }
    }
    //Kac adet randevu var
    public  Long count() {
        return appointmentRepository.count();
    }









}
