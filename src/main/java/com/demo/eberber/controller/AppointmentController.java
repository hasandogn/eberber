package com.demo.eberber.controller;

import com.demo.eberber.domain.Appointment;
import com.demo.eberber.exception.ResourceNotFoundException;
import com.demo.eberber.exception.BadResourceException;
import com.demo.eberber.exception.ResourceAlreadyExistsException;
import com.demo.eberber.service.AppointmentService;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class AppointmentController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping(value = "/Appoinments", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Appointment>> findAll (
            @RequestParam(value ="page", defaultValue = "1") int pageNumber,
            @RequestParam(required = false) Long id ) {
        if(StringUtils.isEmpty(id)) {
            return ResponseEntity.ok(appointmentService.findAll(pageNumber, 5));
        }
        else {
            return ResponseEntity.ok(appointmentService.findAllByBarberId(id));
        }
    }

    @GetMapping(value = "/appointmets/{barberId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Appointment> findAppointmentsWithBarberId(@PathVariable long barberId) {
        try{
            Appointment ap = appointmentService.findById(barberId);
            return  ResponseEntity.ok(ap);
        } catch (ResourceNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();//409
        }
    }
   /* @GetMapping(value = "appointments/{barberId}/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE);
    public  ResponseEntity<Appointment> findAppointmentsWithBarberIdandServiceId(@PathVariable long barberId,
                                                                                 @PathVariable long serviceId) {
        try{
            return ResponseEntity.ok(appointmentService.findServiceIdandBarberId(barberId, serviceId));
        } catch (ResourceNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();//409
        }

    }*/
    /*@GetMapping(value = "appointments/{barberId}/{appointDate}", produces = MediaType.APPLICATION_JSON_VALUE)
    public  ResponseEntity<Appointment> findAppointmentsWithDateandBarberId(@PathVariable long barberId, @PathVariable Date date) {
        try{
            return  ResponseEntity.ok(appointmentService.findDateandBarberId(barberId, date));
        }catch (ResourceNotFoundException e) {
            logger.error(e.getMessage());
            return  ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }*/
   /* @GetMapping(value = "appointments/{barberId}/{appointDate}/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Appointment> findAppointmentsWithBarberIdandDateandServiceId(@PathVariable long barberId,
                                                                                       @PathVariable Date date,
                                                                                       @PathVariable long serviceId) {
        try {
            return ResponseEntity.ok(appointmentService.findBarberIdandDateandServiceId(barberId, date, serviceId));
        }catch (ResourceNotFoundException e) {
            logger.error(e.getMessage());
            return  ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }*/
    @PostMapping(value = "/barbers")
    public ResponseEntity<Appointment> addAppointment(@Valid @RequestBody Appointment appointment)
        throws URISyntaxException {
        try {
            Appointment newAppointment = appointmentService.save(appointment);
            return ResponseEntity.created(new URI("/appointments/" + newAppointment.getId())).body(appointment);
        } catch (ResourceNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (BadResourceException | ResourceAlreadyExistsException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }
    @PutMapping("/appointments/{appointmentId}")
    public ResponseEntity<Appointment> updateAppointment(@Valid @RequestBody Appointment appointment,
                                                         @PathVariable long id) {
        try {
            appointment.setId(id);
            appointmentService.update(appointment);
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
    @DeleteMapping(path = "/appointments/{id}")
    public ResponseEntity<Appointment> deleteAppointmentById(@PathVariable long id ) {
        try {
            appointmentService.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
        /* @GetMapping("/students/{studentId}/courses/{courseId}")
    public Appointment retrieveDetailsForCourse(@PathVariable String studentId,
                                           @PathVariable String courseId) {
        return appointmentService.findDateandBarberId(studentId, courseId);
    }*/
}
