package com.demo.eberber.controller;
import com.demo.eberber.Dto.GeneralDto;
import com.demo.eberber.domain.ServiceBarber;
import com.demo.eberber.exception.ResourceNotFoundException;
import com.demo.eberber.exception.BadResourceException;
import com.demo.eberber.exception.ResourceAlreadyExistsException;

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
    public Object findAll (
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
    public Object findServiceWithBarberId(@PathVariable int barberId) {
        try{
            return serviceBarberService.findAllByBarberId(barberId);
        } catch (ResourceNotFoundException e) {
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);//409
        }
    }

    @PostMapping(value = "/add")
    public Object addService(@Valid @RequestBody ServiceBarber serviceBarber)
            throws URISyntaxException {
        try {
                return serviceBarberService.save(serviceBarber);
        }  catch (BadResourceException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }

    @PutMapping(value="/put/{id}")
    public Object updateService(@Valid @RequestBody ServiceBarber serviceBarber, @PathVariable int id ) {
        try {
            serviceBarber.setId((long) id);
            return serviceBarberService.update(serviceBarber);
        }catch (ResourceNotFoundException ex) {
            // log exception first, then return Not Found (404)
            logger.error(ex.getMessage());
            return ex.getMessage();
        } catch (BadResourceException ex) {
            // log exception first, then return Bad Request (400)
            logger.error(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping(path = "/delete/{id}")
    public Object deleteService(@PathVariable long id ) throws BadResourceException {
        try {

            return ResponseEntity.ok(serviceBarberService.deleteById( id));
        } catch (BadResourceException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
