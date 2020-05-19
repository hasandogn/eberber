package com.demo.eberber.controller;

import com.demo.eberber.Dto.AppointmentDto;
import com.demo.eberber.domain.Appointment;
import com.demo.eberber.Dto.AppointmentDto.*;
import com.demo.eberber.exception.ResourceNotFoundException;
import com.demo.eberber.exception.BadResourceException;
import com.demo.eberber.exception.ResourceAlreadyExistsException;
import com.demo.eberber.service.AppointmentService;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
@RequestMapping("/")
public class AppointmentController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping(value = "/Appointments", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Appointment>> findAll (
            @RequestParam(value ="page", defaultValue = "1") int pageNumber,
            @RequestParam(required = false) Long id ) throws ResourceNotFoundException {
        if(StringUtils.isEmpty(id)) {
            return ResponseEntity.ok(appointmentService.findAll(pageNumber, 500));
        }
        else {
            return ResponseEntity.ok(appointmentService.findAllByBarberId(Math.toIntExact(id)));
        }
    }

    @GetMapping(value = "/Appointments/barber/{barberId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Appointment>> findAppointmentsWithBarberId(@PathVariable int barberId) {
        try{
            List<Appointment> ap = appointmentService.findAllByBarberId(barberId);
            return  ResponseEntity.ok(ap);
        } catch (ResourceNotFoundException e) {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);//409
        }
    }
    //Calisana Gore randevuları getirir.
    @GetMapping(value = "/Appointments/staff/{staffId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public  ResponseEntity<List<Appointment>> findAppointmentsWithStaffId(@PathVariable long staffId) {
        try{
            return ResponseEntity.ok(appointmentService.findAllByStaffId(staffId));
        } catch (ResourceNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();//409
        }
    }
    //Berberin belirlediği tarihteki randebuları döndürülür.
    @PostMapping(value = "/Appointments/barberFilter", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Appointment>> findAppointmentsWithDateandBarberId(@Valid @RequestBody Appointment appointment) {
        try{
            return  ResponseEntity.ok(appointmentService.findDateandBarberId(appointment.getAppointmentDate(), appointment.getBarberId()));
        }catch (Exception e) {
            logger.error(e.getMessage());
            return  ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
    @PostMapping(value = "/Appointments/staffFilter", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Appointment>> findAppointmentsWithDateandStaffId(@Valid @RequestBody Appointment appointment) {
        try{
            return  ResponseEntity.ok(appointmentService.findDateandBarberId(appointment.getAppointmentDate(), appointment.getStaffId()));
        }catch (Exception e) {
            logger.error(e.getMessage());
            return  ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
    @PostMapping(value = "/Appointments/withDate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Appointment>> findAppointmentsWithDate(@RequestBody Appointment appointment) {
        try{
            return  ResponseEntity.ok(appointmentService.findAllByDate(appointment.getAppointmentDate()));
        }catch (Exception e) {
            logger.error(e.getMessage());
            return  ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }


    //Berberin belirlediği tarihteki randebuları döndürülür.
    @PostMapping(value = "/Appointments/customerFilter", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Appointment>> findAppointmentsWithDateandCustomerId(@RequestBody Appointment appointment) {
        try{
            return  ResponseEntity.ok(appointmentService.findDateandCustomerId(appointment.getAppointmentDate(), appointment.getCustomerId()));
        }catch (Exception e) {
            logger.error(e.getMessage());
            return  ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping(value = "/Appointments/customer/{customerId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Appointment>> findAppointmentsWithCustomerId(@PathVariable long customerId) {
        try{
            List<Appointment> ap = appointmentService.findAllByCustomerId(customerId);
            return ResponseEntity.ok(ap);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);//409
        }
    }

    @PostMapping(value = "/Appointments/dateBefore/customer", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Appointment>> findCustomerDateBefore(@RequestBody Appointment appointment) {
        try{
            return  ResponseEntity.ok(appointmentService.findByCustomerDateBefore(appointment.getCustomerId(), appointment.getAppointmentDate()));
        }catch (Exception e) {
            logger.error(e.getMessage());
            return  ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping(value = "/Appointments/monthly/customer", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Appointment>> findCustomerMonthly(@RequestBody Appointment appointment) {
        try{
            return  ResponseEntity.ok(appointmentService.findByCustomerIdMonthly(appointment.getCustomerId()));
        }catch (Exception e) {
            logger.error(e.getMessage());
            return  ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
    @PostMapping(value = "/Appointments/monthly/barber", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Appointment>> findBarberMonthly(@RequestBody Appointment appointment) {
        try{
            return  ResponseEntity.ok(appointmentService.findByBarberIdMonthly(appointment.getBarberId()));
        }catch (Exception e) {
            logger.error(e.getMessage());
            return  ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping(value = "/Appointments/monthly/staff", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Appointment>> findStaffMonthly(@RequestBody Appointment appointment) {
        try{
            return  ResponseEntity.ok(appointmentService.findByStaffIdMonthly(appointment.getStaffId()));
        }catch (Exception e) {
            logger.error(e.getMessage());
            return  ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping(value = "/Appointments/dateBefore/barber", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Appointment>> findBarberDateBefore(@RequestBody Appointment appointment) {
        try{
            return  ResponseEntity.ok(appointmentService.findByBarberDateBefore(appointment.getBarberId(), appointment.getAppointmentDate()));
        }catch (Exception e) {
            logger.error(e.getMessage());
            return  ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping(value = "/Appointments/dateBefore/staff", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Appointment>> findStaffDateBefore(@RequestBody Appointment appointment) {
        try{
            return  ResponseEntity.ok(appointmentService.findByBarberDateBefore(appointment.getStaffId(), appointment.getAppointmentDate()));
        }catch (Exception e) {
            logger.error(e.getMessage());
            return  ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }


    @PostMapping(value = "/Appointments/add")
    public ResponseEntity<Appointment> addAppointment(@Valid @RequestBody Appointment appointment)
        throws URISyntaxException {
        try {
            Appointment newAppointment = appointmentService.save(appointment);
            return ResponseEntity.created(new URI("/Appointments/add/" + newAppointment.getId())).body(appointment);
        } catch (ResourceNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (BadResourceException | ResourceAlreadyExistsException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (ParseException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @PutMapping(value="/Appointments/put/{id}")
    public ResponseEntity<Appointment> updateAppointment(@Valid @RequestBody Appointment appointment,@PathVariable int id ) {
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
    @DeleteMapping(path = "/Appointments/delete/{id}")
    public ResponseEntity<Appointment> deleteAppointmentById(@PathVariable long id ) {
        try {
            appointmentService.deleteById( id);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException | BadResourceException | ParseException e) {
            logger.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

}
