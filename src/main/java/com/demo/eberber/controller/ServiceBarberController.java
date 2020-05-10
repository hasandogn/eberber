package com.demo.eberber.controller;
import com.demo.eberber.domain.ServiceBarber;
import com.demo.eberber.exception.ResourceNotFoundException;
import com.demo.eberber.exception.BadResourceException;
import com.demo.eberber.exception.ResourceAlreadyExistsException;
import com.demo.eberber.service.AppointmentService;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import javax.validation.Valid;

import com.demo.eberber.service.ServiceBarberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ServiceBarber")
public class ServiceBarberController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ServiceBarberService serviceBarberService;

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ServiceBarber>> findAll (
            @RequestParam(value ="page", defaultValue = "1") int pageNumber,
            @RequestParam(required = false) Long id ) throws ResourceNotFoundException {
        if(StringUtils.isEmpty(id)) {
            return ResponseEntity.ok(serviceBarberService.findAll(pageNumber, 500));
        }
        else {
            return ResponseEntity.ok(serviceBarberService.findAllByBarberId(Math.toIntExact(id)));
        }
    }

    @GetMapping(value = "/Barber/{barberId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ServiceBarber>> findServiceWithBarberId(@PathVariable int barberId) {
        try{
            List<ServiceBarber> ap = serviceBarberService.findAllByBarberId(barberId);
            return  ResponseEntity.ok(ap);
        } catch (ResourceNotFoundException e) {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);//409
        }
    }

    @PostMapping(value = "/add")
    public ResponseEntity<ServiceBarber> addService(@Valid @RequestBody ServiceBarber serviceBarber)
            throws URISyntaxException {
        try {
            ServiceBarber newService = serviceBarberService.save(serviceBarber);
            return ResponseEntity.created(new URI("/Appointments/add/" + newService.getId())).body(serviceBarber);
        } catch (ResourceNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (BadResourceException | ResourceAlreadyExistsException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }

    @PutMapping(value="/put/{id}")
    public ResponseEntity<ServiceBarber> updateService(@Valid @RequestBody ServiceBarber serviceBarber,@PathVariable int id ) {
        try {
            serviceBarber.setId((long) id);
            serviceBarberService.update(serviceBarber);
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
    public ResponseEntity<ServiceBarber> deleteService(@PathVariable long id ) {
        try {
            serviceBarberService.deleteById( id);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
