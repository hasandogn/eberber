package com.demo.eberber.controller;

import com.demo.eberber.domain.Staff;
import com.demo.eberber.exception.BadResourceException;
import com.demo.eberber.exception.ResourceAlreadyExistsException;
import com.demo.eberber.exception.ResourceNotFoundException;
import com.demo.eberber.service.StaffService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/Staffs")
public class StaffController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private StaffService staffService;

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Staff>> findAll (
            @RequestParam(value ="page", defaultValue = "1") int pageNumber,
            @RequestParam(required = false) Long id ) throws ResourceNotFoundException {
        if(StringUtils.isEmpty(id)) {
            return ResponseEntity.ok(staffService.findAll(pageNumber, 500));
        }
        else {
            return ResponseEntity.ok(staffService.findAllByBarberId(Math.toIntExact(id)));
        }
    }

    @GetMapping(value = "/barber/{barberId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Staff>> findStaffsWithBarberId(@PathVariable int barberId) {
        try{
            List<Staff> ap = staffService.findAllByBarberId(barberId);
            return  ResponseEntity.ok(ap);
        } catch (ResourceNotFoundException e) {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);//409
        }
    }

    @PostMapping(value = "/add")
    public ResponseEntity<Staff> addStaff(@Valid @RequestBody Staff staff)
            throws URISyntaxException {
        try {
            Staff newStaff = staffService.save(staff);
            return ResponseEntity.created(new URI("/add/" + newStaff.getId())).body(staff);
        } catch (ResourceNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (BadResourceException | ResourceAlreadyExistsException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }

    @PutMapping(value="/put/{id}")
    public ResponseEntity<Staff> updateStaff(@Valid @RequestBody Staff staff, @PathVariable int id ) {
        try {
            staff.setId(id);
            staffService.update(staff);
            return ResponseEntity.ok().build();
        }catch (ResourceNotFoundException ex) {
            // log exception first, then return Not Found (404)
            logger.error(ex.getMessage());
            return ResponseEntity.notFound().build();
        } catch (BadResourceException ex) {
            // log exception first, then return Bad Request (400)
            logger.error(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping(path = "/Appointments/delete/{id}")
    public ResponseEntity<Staff> deleteStaffById(@PathVariable long id ) {
        try {
            staffService.deleteById( id);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
