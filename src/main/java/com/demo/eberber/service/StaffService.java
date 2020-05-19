package com.demo.eberber.service;

import com.demo.eberber.domain.Staff;
import com.demo.eberber.domain.Barber;
import com.demo.eberber.exception.BadResourceException;
import com.demo.eberber.exception.ResourceAlreadyExistsException;
import com.demo.eberber.exception.ResourceNotFoundException;
import com.demo.eberber.repository.StaffRepository;
import com.demo.eberber.specification.StaffSpecification;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class StaffService {
    @Autowired
    private StaffRepository staffRepository;

    private boolean existById(Long i) {
        return staffRepository.existsById(i);
    }

    public Staff findById(long id) throws ResourceNotFoundException {
        Staff staff = staffRepository.findById(id).orElse(null);
        if (staff == null)
            throw new ResourceNotFoundException("Cannot find staff with id :" + id);
        else return staff;
    }

    public List<Staff> findAll() throws  ResourceNotFoundException {
        List<Staff> staff = new ArrayList<>();
        staffRepository.findAll().forEach(staff::add);
        if(staff == null)
            throw new ResourceNotFoundException("No staff were found.\n");
        else
            return staff;
    }

    public List<Staff> findAllByBarberId(long id) throws ResourceNotFoundException{
        Staff filter = new Staff();
        filter.setBarberId(id);
        Specification<Staff> spec = new StaffSpecification(filter);

        List<Staff> appointmentsBarber = new ArrayList<>();
        Iterable<Staff> i = staffRepository.findByBarberId(id);
        i.forEach(appointmentsBarber::add);
        if(appointmentsBarber == null)
            throw  new ResourceNotFoundException("Cannot find staff with barber id:" + id);
        else
            return appointmentsBarber;
    }


    public List<Long> findStaffIdWithId(long id) {
        List<Long> staffIds = new ArrayList<>();
        Iterable<Long> i=staffRepository.findWithBarberId(id);
        i.forEach(staffIds::add);
        // barberRepository.findAll(PageRequest.of(pageNumber-1, rowPerPage)).forEach(barbers::add);
        return staffIds;
    }

    public Staff save(Staff staff) throws BadResourceException, ResourceNotFoundException,ResourceAlreadyExistsException {
        if(!StringUtils.isEmpty(staff.getBarberId())){
            if(staff.getId() != 0 && existById((long) staff.getId()))
                throw  new ResourceNotFoundException("Staff with id " + staff.getId() + "already exists" );
            return  staffRepository.save(staff);
        }
        else {
            BadResourceException exc = new BadResourceException("Failed to save staff");
            exc.addErrorMessage("Staff is null or empty");
            throw exc;
        }
    }

    public Staff update(Staff staff) throws ResourceNotFoundException, BadResourceException {
        if(!existById(staff.getBarberId())) {
            if(existById( staff.getId()))
                throw new ResourceNotFoundException("Staff find Contact with id: " + staff.getId());
            return staffRepository.save(staff);
        }
        else {
            BadResourceException exc = new BadResourceException("Failed to save staff");
            exc.addErrorMessage("Staff is null or empty");
            throw exc;
        }
    }

    public void deleteById(Long id) throws ResourceNotFoundException  {
        if(!existById( id)) {
            throw new ResourceNotFoundException("Cannot find staff with id: " + id);
        }
        else {
            staffRepository.deleteById(id);
        }
    }

}
