package com.demo.eberber.service;

import com.demo.eberber.domain.Appointment;
import com.demo.eberber.domain.HoursStatus;
import com.demo.eberber.domain.ServiceBarber;
import com.demo.eberber.domain.WorkHours;
import com.demo.eberber.exception.BadResourceException;
import com.demo.eberber.exception.ResourceAlreadyExistsException;
import com.demo.eberber.exception.ResourceNotFoundException;
import com.demo.eberber.repository.HoursStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.temporal.TemporalAccessor;
import java.util.*;

@Service
public class HoursStatusService {
    @Autowired
    private HoursStatusRepository hoursStatusRepository;

    private boolean existById(Long i) { return hoursStatusRepository.existsById(i);}



    public HashMap<String, List<String>> freeHoursfindByStaffIdAndDay (long staffId, String day) throws HttpClientErrorException.BadRequest, ResourceNotFoundException {
            //HourStatusDto.WeeklyHours dayAndHour = null;
            HashMap<String,List<String>> daysAndHours = new HashMap<String, List<String>>();
            List<String> hours = new ArrayList<>();
            Iterable<String> i = hoursStatusRepository.findByStaffIdAndDayAndEmptyIsHoursOrderByHourAsc(staffId, day,"true");

            i.forEach(hours::add);
            daysAndHours.put(day, hours);
            if (hours == null) {
                throw  new ResourceNotFoundException("No free hours was staff.\n");
            }
            else
                return daysAndHours;
    }

    public HashMap<String, List<String>> freeHoursfindByStaffIdAllWeek (long staffId) throws HttpClientErrorException.BadRequest, ResourceNotFoundException {

        HashMap<String,List<String>> daysAndHours = new HashMap<String, List<String>>();
        List<String> days = new ArrayList<>();
        Iterable<String> daysIterable = hoursStatusRepository.findDistinctByStaffId(staffId);
        daysIterable.forEach(days::add);
        List<String> hours = new ArrayList<>();
        for(int k = 0; k < days.size() ; k++ ){
            Iterable<String> i = hoursStatusRepository.findByStaffIdAndDayAndEmptyIsHoursOrderByHourAsc(staffId, days.get(k),"true");
            i.forEach(hours::add);
            daysAndHours.put(days.get(k), hours);
        }
        return daysAndHours;
    }



    //Calisana calisma saati eklendiğinde Uygun randevu saatleri eklenir.
    public void save(WorkHours workHours) throws BadResourceException, ResourceAlreadyExistsException, ParseException {
        long halfHour = 1000 * 60 * 15 ;
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        if(!StringUtils.isEmpty(workHours.getStartHour()) && !StringUtils.isEmpty(workHours.getEndHour()) && !StringUtils.isEmpty(workHours.getStaffId())) {
            String startHour = workHours.getStartHour();
            String endHour = workHours.getEndHour();
            Date dateStart = formatter.parse(startHour);
            Date dateEnd = formatter.parse(endHour);
            for( Date dt = dateStart; dt.compareTo(dateEnd) < 0; dt = new Date(dt.getTime() +  halfHour) ) {
                HoursStatus hoursStatus = new HoursStatus();
                String h = formatter.format(dt);
                hoursStatus.setDay(workHours.getDay());
                hoursStatus.setHour(h);
                hoursStatus.setEmptyIsHours("true");
                hoursStatus.setStaffId(workHours.getStaffId());
                hoursStatusRepository.save(hoursStatus);
            }
        }
        else {
            BadResourceException exc = new BadResourceException();
            exc.addErrorMessage("Customer is null or empty");
            throw exc;
        }
    }

    public void whenAddAppointmentUpdate (Appointment appointment) throws ParseException, BadResourceException {
        long quarterHour =  1000 * 60 * 15;
        DateFormat formatHour = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        DateFormat formatDay = new SimpleDateFormat("EEEE", Locale.ENGLISH);
        if(!StringUtils.isEmpty(appointment.getAppointmentDate()) && !StringUtils.isEmpty(appointment.getAppointmentEndDate())){
            for( Date dt = appointment.getAppointmentDate(); dt.compareTo(appointment.getAppointmentEndDate()) < 0; dt = new Date(dt.getTime() +  quarterHour) ) {
                String day = formatDay.format(dt);
                Date dt3 = new Date((dt.getTime() - (1000 * 60 * 60 *3)));
                String hour = formatHour.format(dt3);
                HoursStatus hoursStatus = new HoursStatus();
                hoursStatus = hoursStatusRepository.findByStaffIdAndDayAndHour(appointment.getStaffId(), day, hour);
                hoursStatus.setEmptyIsHours("false");
                try {
                    update(hoursStatus);
                } catch (BadResourceException e) {
                    e.printStackTrace();
                } catch (ResourceNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }else {
            BadResourceException exc = new BadResourceException("Failed to save work hours");
            exc.addErrorMessage("Dates is null or empty");
            throw exc;
        }
    }

    public String thereIsStaffAppointments (Appointment appointment) throws ParseException, BadResourceException {
        long quarterHour =  1000 * 60 * 15;
        DateFormat formatHour = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        DateFormat formatDay = new SimpleDateFormat("EEEE", Locale.ENGLISH);
        if(!StringUtils.isEmpty(appointment.getAppointmentDate()) && !StringUtils.isEmpty(appointment.getAppointmentEndDate())){
            for( Date dt = appointment.getAppointmentDate(); dt.compareTo(appointment.getAppointmentEndDate()) < 0; dt = new Date(dt.getTime() +  quarterHour) ) {
                String day = formatDay.format(dt);
                Date dt3 = new Date((dt.getTime() - (1000 * 60 * 60 *3)));
                String hour = formatHour.format(dt3);
                HoursStatus hoursStatus = new HoursStatus();
                hoursStatus = hoursStatusRepository.findByStaffIdAndDayAndHour(appointment.getStaffId(), day, hour);
                String result = hoursStatus.getEmptyIsHours();
                if(result == "false")
                    return "false";
            }
            return "true";
        }
        else {
            BadResourceException exc = new BadResourceException("Failed to save work hours");
            exc.addErrorMessage("Dates is null or empty");
            //throw exc;
            return null;
        }
    }

    public void whenDeleteAppointmentUpdate (Appointment appointment) throws ParseException, BadResourceException {
        long quarterHour =  1000 * 60 * 15;
        DateFormat formatHour = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        DateFormat formatDay = new SimpleDateFormat("EEEE", Locale.ENGLISH);
        if(!StringUtils.isEmpty(appointment.getAppointmentDate()) && !StringUtils.isEmpty(appointment.getAppointmentEndDate())){
            for( Date dt = appointment.getAppointmentDate(); dt.compareTo(appointment.getAppointmentEndDate()) < 0; dt = new Date(dt.getTime() +  quarterHour) ) {
                String day = formatDay.format(dt);
                Date dt3 = new Date((dt.getTime() - (1000 * 60 * 60 *3)));
                String hour = formatHour.format(dt3);
                HoursStatus hoursStatus = new HoursStatus();
                hoursStatus = hoursStatusRepository.findByStaffIdAndDayAndHour(appointment.getStaffId(), day, hour);
                hoursStatus.setEmptyIsHours("true");
                try {
                    update(hoursStatus);
                } catch (BadResourceException e) {
                    e.printStackTrace();
                } catch (ResourceNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }else {
            BadResourceException exc = new BadResourceException("Failed to save work hours");
            exc.addErrorMessage("Dates is null or empty");
            throw exc;
        }
    }

    public void whenDeleteWorkHoursDeleted (WorkHours workHours) throws ParseException, BadResourceException {
        long quarterHour =  1000 * 60 * 15;
        DateFormat formatHour = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        Date startHour = formatHour.parse(workHours.getStartHour());
        Date endHour = formatHour.parse(workHours.getEndHour());
        if(!StringUtils.isEmpty(startHour) && !StringUtils.isEmpty(endHour)){
            for( Date dt = startHour; dt.compareTo(endHour) < 0; dt = new Date(dt.getTime() +  quarterHour) ) {
                //Date dt3 = new Date((dt.getTime() - (1000 * 60 * 60 *3)));
                String hour = formatHour.format(dt);
                HoursStatus hoursStatus = new HoursStatus();
                hoursStatus = hoursStatusRepository.findByStaffIdAndDayAndHour(workHours.getStaffId(), workHours.getDay(), hour);
                try {
                    deleteById(hoursStatus.getId());
                }
                catch (ResourceNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }else {
            BadResourceException exc = new BadResourceException("Failed to save work hours");
            exc.addErrorMessage("Dates is null or empty");
            throw exc;
        }
    }

    public void update(HoursStatus hoursStatus)
            throws BadResourceException, ResourceNotFoundException {
        if (!StringUtils.isEmpty(hoursStatus.getStaffId())) {
            if (!existById(Long.valueOf(hoursStatus.getId()))) {
                throw new ResourceNotFoundException("Cannot find Staff with id: " + hoursStatus.getId());
            }
            hoursStatusRepository.save(hoursStatus);
        }
        else {
            BadResourceException exc = new BadResourceException("Failed to save staff hour.");
            exc.addErrorMessage("Staff is null or empty");
            throw exc;
        }
    }

    public void deleteById(Long id) throws ResourceNotFoundException  {
        if(!existById( id)) {
            throw new ResourceNotFoundException("Cannot find hours with id: " + id);
        }
        else {
            hoursStatusRepository.deleteById(id);
            throw new ResourceNotFoundException("Delete hours with id: " + id);
        }
    }
}
