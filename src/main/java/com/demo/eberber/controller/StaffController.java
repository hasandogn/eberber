package com.demo.eberber.controller;

import com.demo.eberber.domain.Appointment;
import com.demo.eberber.domain.HoursStatus;
import com.demo.eberber.domain.Staff;
import com.demo.eberber.exception.BadResourceException;
import com.demo.eberber.exception.ResourceAlreadyExistsException;
import com.demo.eberber.exception.ResourceNotFoundException;
import com.demo.eberber.service.AppointmentService;
import com.demo.eberber.service.HoursStatusService;
import com.demo.eberber.service.StaffService;
import com.demo.eberber.service.WorkHoursService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/")
public class StaffController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private StaffService staffService;
    @Autowired
    private HoursStatusService hoursStatusService;
    @Autowired
    private WorkHoursService workHoursService;
    @Autowired
    private AppointmentService appointmentService;

    @GetMapping(value = "/Staffs", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Staff>> findAll () throws ResourceNotFoundException {
        List<Staff> allStaff = staffService.findAll();
            return ResponseEntity.ok(allStaff);
    }

    @GetMapping(value = "/Staffs/getStaff/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Staff> getStaff (@PathVariable Long id) throws ResourceNotFoundException {
        try{
            return ResponseEntity.ok(staffService.findById(id));
        } catch (ResourceNotFoundException ex) {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);//409
        }
    }

    @GetMapping(value = "/Staffs/barber/{barberId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Staff>> findStaffsWithBarberId(@PathVariable long barberId) {
        try{
            List<Staff> ap = staffService.findAllByBarberId(barberId);
            return  ResponseEntity.ok(ap);
        } catch (ResourceNotFoundException e) {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);//409
        }
    }


    @GetMapping(value = "/Staffs/Weekly/FreeHours/{staffId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, List<String>>> findByStaffIdWeeklyFreeHours(@PathVariable long staffId) {
        try{
            HashMap<String,List<String>> daysAndHours = new HashMap<String, List<String>>();
            daysAndHours = hoursStatusService.freeHoursfindByStaffIdAllWeek(staffId);
            return  ResponseEntity.ok(daysAndHours);
        } catch (ResourceNotFoundException e) {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);//409
        }
    }

    @PostMapping(value = "/Staffs/Daily/FreeHours",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, List<String>>> findByStaffIdDailyFreeHours(@Valid @RequestBody HoursStatus hoursStatus) {
        try{
            HashMap<String,List<String>> daysAndHours = new HashMap<String, List<String>>();
            daysAndHours = hoursStatusService.freeHoursfindByStaffIdAndDay(hoursStatus.getStaffId(),hoursStatus.getDay());
            return  ResponseEntity.ok(daysAndHours);
        } catch (ResourceNotFoundException e) {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);//409
        }
    }

    @PostMapping(value = "Staffs/add")
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

    @PutMapping(value="Staffs/put/{id}")
    public ResponseEntity<Staff> updateStaff(@Valid @RequestBody Staff staff, @PathVariable int id ) {
        try {
            staff.setId(id);
            staffService.update(staff);
            return ResponseEntity.ok(staff);
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

    @DeleteMapping(path = "/Staffs/delete/{id}")
    public ResponseEntity<Staff> deleteStaffById(@PathVariable long id ) throws ResourceAlreadyExistsException {
        try {
            Date nowDate = new Date();
            List<Appointment> staffAppointments = new ArrayList<>();
            staffAppointments = appointmentService.findByStaffDateBefore(id,nowDate);
            if(staffAppointments.size() == 0){
                staffService.deleteById( id);
                List<Long> workHoursIds = new ArrayList<>();
                workHoursIds = workHoursService.findIdWithStaffId(id);
                for (int i = 0 ; i<workHoursIds.size(); i++) {
                    workHoursService.deleteById(workHoursIds.get(i));
                }
                return ResponseEntity.ok().build();
            }
            else {
                Staff thisStaff = new Staff();
                thisStaff = staffService.findById(id);
                List<Long> otherBarberStaffs = new ArrayList<>();
                otherBarberStaffs = staffService.findStaffIdWithId(thisStaff.getBarberId());

                if(otherBarberStaffs.size() == 0) {
                    return null;
                }
                else if(otherBarberStaffs.size() > 0) {
                    for(int ap=0; ap<staffAppointments.size(); ap++ ){
                        for(int k = 0; k<otherBarberStaffs.size(); k++){
                            Long cotntrolStaff = otherBarberStaffs.get(k);
                            if(cotntrolStaff == thisStaff.getId()){
                                continue;
                            }
                            String statusStaffsAppointment = hoursStatusService.thereIsStaffAppointments(staffAppointments.get(ap));
                            if(statusStaffsAppointment == "true"){
                                Appointment transferAppointment = new Appointment();
                                transferAppointment = staffAppointments.get(ap);
                                transferAppointment.setStaffId(Math.toIntExact(otherBarberStaffs.get(k)));
                                workHoursService.findAllByDayAndStaffId(String.valueOf(staffAppointments.get(ap)), thisStaff.getId());
                                appointmentService.save(transferAppointment);
                                appointmentService.deleteById(staffAppointments.get(ap).getId());
                                List<Long> workHoursIds = new ArrayList<>();
                                workHoursIds = workHoursService.findIdWithStaffId(id);
                                for (int i = 0 ; i<workHoursIds.size(); i++) {
                                    workHoursService.deleteById(workHoursIds.get(i));
                                    if(ap == staffAppointments.size()){
                                        staffService.deleteById( id);
                                        return ResponseEntity.ok().build();
                                    }
                                }
                                continue;
                            }
                        }
                    }
                    return null;
                }
            }

            staffService.deleteById( id);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException | ParseException | BadResourceException e) {
            logger.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
