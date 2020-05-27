package com.demo.eberber.service;

import com.demo.eberber.domain.ServiceBarber;
import com.demo.eberber.exception.BadResourceException;
import com.demo.eberber.exception.ResourceAlreadyExistsException;
import com.demo.eberber.exception.ResourceNotFoundException;
import com.demo.eberber.repository.ServiceBarberRepository;
import com.demo.eberber.specification.AppointmentSpecification;
import com.demo.eberber.specification.ServiceBarberSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceBarberService {
    @Autowired
    private ServiceBarberRepository serviceBarberRepository;

    private boolean existById(Long i) { return serviceBarberRepository.existsById(i);}

    public ServiceBarber findById(long id) throws ResourceNotFoundException {
        ServiceBarber serviceBarber = serviceBarberRepository.findById(id).orElse(null);
        if(serviceBarber == null)
            throw new ResourceNotFoundException("Cannot find service with id :" +id);
        else return serviceBarber;
    }

    public List<ServiceBarber> findAll(int pageNumber, int rowPerPage) throws  ResourceNotFoundException {
        List<ServiceBarber> services = new ArrayList<>();
        serviceBarberRepository.findAll(PageRequest.of(pageNumber-1,rowPerPage)).forEach(services::add);
        if(services == null)
            throw new ResourceNotFoundException("No service were found.\n");
        else
            return services;
    }

    public List<ServiceBarber> findAllByBarberId(int id) throws ResourceNotFoundException{
        ServiceBarber filter = new ServiceBarber();
        filter.setBarberId(id);
        Specification<ServiceBarber> spec = new ServiceBarberSpecification(filter);

        List<ServiceBarber> appointmentsBarber = new ArrayList<>();
        Iterable<ServiceBarber> i = serviceBarberRepository.findByBarberId(id);
        i.forEach(appointmentsBarber::add);
        if(appointmentsBarber == null)
            throw  new ResourceNotFoundException("Cannot find Service with barber id:" + id);
        else
            return appointmentsBarber;
    }

    public ServiceBarber save(ServiceBarber serviceBarber) throws BadResourceException, ResourceNotFoundException, ResourceAlreadyExistsException {
        if(!StringUtils.isEmpty(serviceBarber.getPrice())){
            if(serviceBarber.getId() != 0 && existById( serviceBarber.getId()))
                throw  new ResourceNotFoundException("Service with id " + serviceBarber.getId() + "already exists" );
            return  serviceBarberRepository.save(serviceBarber);
        }
        else {
            BadResourceException exc = new BadResourceException("Failed to save Service");
            exc.addErrorMessage("Service is null or empty");
            throw exc;
        }
    }

    public ServiceBarber update(ServiceBarber serviceBarber) throws ResourceNotFoundException, BadResourceException {
        if(existById((long) serviceBarber.getBarberId())) {
            if(!existById((long) serviceBarber.getId()))
                throw new ResourceNotFoundException("Appointment find Contact with id: " + serviceBarber.getId());
            return serviceBarberRepository.save(serviceBarber);
        }
        else {
            BadResourceException exc = new BadResourceException("Failed to save appointment");
            exc.addErrorMessage("Appointment is null or empty");
            throw exc;
        }
    }

    public void deleteById(Long id) throws ResourceNotFoundException  {
        if(!existById( id)) {
            throw new ResourceNotFoundException("Cannot find service with id: " + id);
        }
        else {
            serviceBarberRepository.deleteById(id);
        }
    }

}
