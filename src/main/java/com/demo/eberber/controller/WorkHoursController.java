package com.demo.eberber.controller;

import com.demo.eberber.Dto.GeneralDto;
import com.demo.eberber.domain.HoursStatus;
import com.demo.eberber.domain.WorkHours;
import com.demo.eberber.exception.ResourceNotFoundException;
import com.demo.eberber.exception.BadResourceException;
import com.demo.eberber.exception.ResourceAlreadyExistsException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.validation.Valid;

import com.demo.eberber.service.HoursStatusService;
import com.demo.eberber.service.WorkHoursService;
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
@RequestMapping("/")
public class WorkHoursController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private WorkHoursService workHoursService;
    @Autowired
    private HoursStatusService hoursStatusService;

    @GetMapping(value = "/WorkHours", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<WorkHours>> findAll (
            @RequestParam(value ="page", defaultValue = "1") int pageNumber,
            @RequestParam(required = false) Long id ) throws ResourceNotFoundException {
        if(StringUtils.isEmpty(id)) {
            return ResponseEntity.ok(workHoursService.findAll(pageNumber, 500));
        }
        else {
            return ResponseEntity.ok(workHoursService.findAllByBarberId(Math.toIntExact(id)));
        }
    }

    @GetMapping(value = "/WorkHours/barber/{barberId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<WorkHours>> findAppointmentsWithBarberId(@PathVariable int barberId) {
        try{
            List<WorkHours> ap = workHoursService.findAllByBarberId(barberId);
            return  ResponseEntity.ok(ap);
        } catch (ResourceNotFoundException e) {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);//409
        }
    }

    @GetMapping(value = "/WorkHours/staff/{staffId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public  ResponseEntity<List<WorkHours>> findAppointmentsWithStaffId(@PathVariable long staffId) {
        try{
            return ResponseEntity.ok(workHoursService.findAllByStaffId(staffId));
        } catch (ResourceNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();//409
        }
    }

    @PostMapping(value = "/WorkHours/barber/day", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<WorkHours>> findWorkHoursWithDayBarberId(@RequestBody WorkHours workHours) {
        try{
            return  ResponseEntity.ok(workHoursService.findAllByDayAndBarberId(workHours.getDay(), workHours.getBarberId()));
        }catch (Exception e) {
            logger.error(e.getMessage());
            return  ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping(value = "/WorkHours/staff/day", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<WorkHours>> findWorkHoursWithDayStaffId(@RequestBody WorkHours workHours) {
        try{
            return  ResponseEntity.ok(workHoursService.findAllByDayAndStaffId(workHours.getDay(), workHours.getStaffId()));
        }catch (Exception e) {
            logger.error(e.getMessage());
            return  ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping(value = "/WorkHours/add")
    public ResponseEntity<WorkHours> addWorkHours(@Valid @RequestBody WorkHours workHours)
            throws URISyntaxException {
        try {
            WorkHours newWorkHours = workHoursService.save(workHours);
            hoursStatusService.save(workHours);
            return ResponseEntity.created(new URI("/WorkHours/add/" + newWorkHours.getId())).body(workHours);
        } catch (BadResourceException | ResourceAlreadyExistsException | ParseException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping(value="/WorkHours/put")
    public ResponseEntity<GeneralDto.Response> updateWorkHours(@Valid @RequestBody WorkHours workHours ) throws ParseException {
        try {
            GeneralDto.Response result = new GeneralDto.Response();
            WorkHours oldWorkHours = workHoursService.findById(workHours.getId());
            boolean hourstatusControl = hoursStatusService.whenDeleteWorkHoursDeleted(oldWorkHours);
            if(hourstatusControl == true){
                hoursStatusService.save(workHours);
                workHoursService.update(workHours);
                result.data = workHours;
                return ResponseEntity.ok(result);
            } else {
                result.Error = true;
                result.Message = "Silmek istediğiniz saatte çalışanın randevusu bulunuyor! ";
                return ResponseEntity.ok(result);
            }
        }catch (ResourceNotFoundException ex) {
            // log exception first, then return Not Found (404)
            logger.error(ex.getMessage());
            return ResponseEntity.notFound().build();
        } catch (BadResourceException | ResourceAlreadyExistsException ex) {
            // log exception first, then return Bad Request (400)
            logger.error(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping(path = "/WorkHours/delete/{id}")
    public ResponseEntity<GeneralDto.Response> deleteWorkHoursById(@PathVariable long id ) {
        try {
            return ResponseEntity.ok(workHoursService.deleteById( id));
        } catch (ResourceNotFoundException | BadResourceException | ParseException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
