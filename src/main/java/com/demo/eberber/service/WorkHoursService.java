package com.demo.eberber.service;

import com.demo.eberber.domain.WorkHours;
import com.demo.eberber.exception.BadResourceException;
import com.demo.eberber.exception.ResourceAlreadyExistsException;
import com.demo.eberber.exception.ResourceNotFoundException;
import com.demo.eberber.repository.WorkHourRepository;
import com.demo.eberber.specification.AppointmentSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class WorkHoursService {
    @Autowired
    private WorkHourRepository workHourRepository;
    @Autowired
    private HoursStatusService hoursStatusService;

    private boolean existsById(long i) {
        return workHourRepository.existsById(i);
    }

    public WorkHours findById(long customerId) throws ResourceNotFoundException {
        WorkHours workHours = workHourRepository.findById(customerId).orElse(null);
        if (workHours==null) {
            throw new ResourceNotFoundException("Cannot find WorkHours with id: " + customerId);
        }
        else return workHours;
    }

    public List<WorkHours> findAll() throws ResourceNotFoundException{
        List<WorkHours> workHoursBarberId = new ArrayList<>();
        Iterable<WorkHours> i = workHourRepository.findAll();
        i.forEach(workHoursBarberId::add);
        if(workHoursBarberId == null)
            throw  new ResourceNotFoundException("Cannot find work hours.");
        else
            return workHoursBarberId;
    }

    public List<WorkHours> findAllByBarberId(long id) throws ResourceNotFoundException{
        List<WorkHours> workHoursBarberId = new ArrayList<>();
        Iterable<WorkHours> i = workHourRepository.findByBarberId(id);
        i.forEach(workHoursBarberId::add);
        if(workHoursBarberId == null)
            throw  new ResourceNotFoundException("Cannot find work hours with barber id:" + id);
        else
            return workHoursBarberId;
    }

    public List<WorkHours> findAllByDayAndBarberId(String day, long id) throws ResourceNotFoundException{
        List<WorkHours> workHoursBarberId = new ArrayList<>();
        Iterable<WorkHours> i = workHourRepository.findByDayAndBarberId(day,id);
        i.forEach(workHoursBarberId::add);
        if(workHoursBarberId == null)
            throw  new ResourceNotFoundException("Cannot find work hours with barber id:" + id + " and day:" + day);
        else
            return workHoursBarberId;
    }

    public List<WorkHours> findAllByDayAndStaffId(String day, long id) throws ResourceNotFoundException{
        List<WorkHours> workHoursBarberId = new ArrayList<>();
        Iterable<WorkHours> i = workHourRepository.findByDayAndStaffId(day,id);
        i.forEach(workHoursBarberId::add);
        if(workHoursBarberId == null)
            throw  new ResourceNotFoundException("Cannot find work hours with barber id:" + id + " and day:" + day);
        else
            return workHoursBarberId;
    }

    public List<WorkHours> findAllByStaffId(long id) throws ResourceNotFoundException{
        List<WorkHours> workHoursStaffId = new ArrayList<>();
        Iterable<WorkHours> i = workHourRepository.findByStaffId(id);
        i.forEach(workHoursStaffId::add);
        if(workHoursStaffId == null)
            throw  new ResourceNotFoundException("Cannot find work hours with barber id:" + id);
        else
            return workHoursStaffId;
    }

    public List<WorkHours> findAllByDay(String day) throws ResourceNotFoundException{
        List<WorkHours> workHoursDay = new ArrayList<>();
        Iterable<WorkHours> i = workHourRepository.findByDay(day);
        i.forEach(workHoursDay::add);
        if(workHoursDay == null)
            throw  new ResourceNotFoundException("Cannot find work hours with day:" + day);
        else
            return workHoursDay;
    }

    public List<WorkHours> findAll(int pageNumber, int rowPerPage) {
        List<WorkHours> Customers = new ArrayList<>();
        workHourRepository.findAll(PageRequest.of(pageNumber - 1, rowPerPage)).forEach(Customers::add);
        return Customers;
    }

    public List<Long> findIdWithStaffId(long staffId) {
        List<Long> hooursIds = new ArrayList<>();
        hooursIds = workHourRepository.findWithStaffId(staffId);
        return hooursIds;
    }

    public WorkHours save(WorkHours workHours) throws BadResourceException, ResourceAlreadyExistsException {
        if (!StringUtils.isEmpty(workHours.getStartHour())) {
            if (workHours.getId() != 0 && existsById(Long.valueOf(workHours.getId()))) {
                throw new ResourceAlreadyExistsException("WorkHours with id: " + workHours.getId() +
                        " already exists");
            }
            return workHourRepository.save(workHours);
        }
        else {
            BadResourceException exc = new BadResourceException();
            exc.addErrorMessage("Customer is null or empty");
            throw exc;
        }
    }

    public void update(WorkHours workHours)
            throws BadResourceException, ResourceNotFoundException {
        if (!StringUtils.isEmpty(workHours.getBarberId())) {
            if (!existsById(Long.valueOf(workHours.getId()))) {
                throw new ResourceNotFoundException("Cannot find Customer with id: " + workHours.getId());
            }
            workHourRepository.save(workHours);
        }
        else {
            BadResourceException exc = new BadResourceException("Failed to save Customer");
            exc.addErrorMessage("Customer is null or empty");
            throw exc;
        }
    }

    public void deleteById(Long id) throws ResourceNotFoundException, BadResourceException, ParseException {
        if (!existsById(id)) {
            throw new ResourceNotFoundException("Cannot find Customer with id: " + id);
        }
        else {
            WorkHours workHours = findById(id);
            hoursStatusService.whenDeleteWorkHoursDeleted(workHours);
            workHourRepository.deleteById(id );
        }
    }
}
