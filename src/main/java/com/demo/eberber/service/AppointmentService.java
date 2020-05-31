package com.demo.eberber.service;

import com.demo.eberber.Dto.GeneralDto;
import com.demo.eberber.domain.Appointment;
import com.demo.eberber.Dto.AppointmentDto.*;
import com.demo.eberber.domain.HoursStatus;
import com.demo.eberber.domain.ServiceBarber;
import com.demo.eberber.exception.BadResourceException;
import com.demo.eberber.exception.ResourceAlreadyExistsException;
import com.demo.eberber.exception.ResourceNotFoundException;
import com.demo.eberber.repository.AppointmentRepository;
import com.demo.eberber.repository.HoursStatusRepository;
import com.demo.eberber.specification.AppointmentSpecification;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/*import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;*/

@Service
public class AppointmentService {
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private  HoursStatusService hoursStatusService;

    private boolean existById(Long i) { return appointmentRepository.existsById(i);}

    public GeneralDto.Response findById(long id) throws ResourceNotFoundException {
        GeneralDto.Response result = new GeneralDto.Response();
        Appointment appointment = appointmentRepository.findById(id).orElse(null);
        if(appointment == null){
            result.Error = true;
            result.Message = "Randevu bulunamadı!";
            return result;
        }
        else {
            result.data = appointment;
            return result;
        }
    }
    //randevuları listelemek
    public GeneralDto.Response findAll(int pageNumber, int rowPerPage) throws  ResourceNotFoundException {
        List<Appointment> appointments = new ArrayList<>();
        GeneralDto.Response result = new GeneralDto.Response();
        appointmentRepository.findAll(PageRequest.of(pageNumber-1,rowPerPage)).forEach(appointments::add);
        if(appointments == null){
            result.Error = true;
            result.Message = "Randevu bulunmuyor!";
            return result;
        }
        else{
            result.data = appointments;
            return result;
        }
    }
    //barber id e gore listelemek
    public GeneralDto.Response findAllByBarberId(long id) throws ResourceNotFoundException{
        Appointment filter = new Appointment();
        GeneralDto.Response result = new GeneralDto.Response();


        filter.setBarberId((int)id);
        Specification<Appointment> spec = new AppointmentSpecification(filter);

        List<Appointment> appointmentsBarber = new ArrayList<>();
        Iterable<Appointment> i = appointmentRepository.findByBarberId(id);
        i.forEach(appointmentsBarber::add);
        if(appointmentsBarber == null){
            result.Error = true;
            result.Message = "Randevu bulunamadı!";
            return result;
        }
        else{
            result.data = appointmentsBarber;
            return result;
        }
    }

    public GeneralDto.Response findAllByCustomerId(long id) throws ResourceNotFoundException{
        Appointment filter = new Appointment();
        GeneralDto.Response result = new GeneralDto.Response();
        filter.setCustomerId((int)id);
        Specification<Appointment> spec = new AppointmentSpecification(filter);

        List<Appointment> appointmentsCustomer = new ArrayList<>();
        Iterable<Appointment> i = appointmentRepository.findByCustomerId(id);
        i.forEach(appointmentsCustomer::add);
        if(appointmentsCustomer == null){
            result.Error = true;
            result.Message = "Randevu bulunamadı!";
            return result;
        }
        else {
            result.data = appointmentsCustomer;
            return result;
        }
    }
    public GeneralDto.Response findAllByStaffId(long id) throws ResourceNotFoundException{
        Appointment filter = new Appointment();
        filter.setCustomerId((int)id);
        Specification<Appointment> spec = new AppointmentSpecification(filter);
        GeneralDto.Response result = new GeneralDto.Response();
        List<Appointment> appointmentsStaff = new ArrayList<>();
        Iterable<Appointment> i = appointmentRepository.findByStaffId(id);
        i.forEach(appointmentsStaff::add);
        if(appointmentsStaff == null){
            result.Error = true;
            result.Message = "Randevu bulunamadı!";
            return result;
        }
        else{
            result.data = appointmentsStaff;
            return result;
        }
    }
    public GeneralDto.Response findAllByDate(Date appointmentDate) throws ResourceNotFoundException{
        Appointment filter = new Appointment();
        filter.setAppointmentDate(appointmentDate);
        Specification<Appointment> spec = new AppointmentSpecification(filter);

        List<Appointment> appointmentsDate = new ArrayList<>();
        GeneralDto.Response result = new GeneralDto.Response();
        Iterable<Appointment> i = appointmentRepository.findByAppointmentDate(appointmentDate);
        i.forEach(appointmentsDate::add);
        if(appointmentsDate == null){
            result.Error = true;
            result.Message = "Randevu bulunamadı!";
            return result;
        }
        else{
            result.data = appointmentDate;
            return result;
        }
    }


    //berber id ile tarihe göre
    public GeneralDto.Response findDateandBarberId (Date appointmentDate, long barberId) throws ResourceNotFoundException {
        List<Appointment> appointmentsBarber = new ArrayList<>();
        GeneralDto.Response result = new GeneralDto.Response();
        Iterable<Appointment> i = appointmentRepository.findByAppointmentDateAndBarberId(appointmentDate, barberId);//findByBarber(appointmentsFilter.date, appointmentsFilter.barberId);
        i.forEach(appointmentsBarber::add);
        if(appointmentsBarber == null){
            result.Error = true;
            result.Message = "Randevu bulunamadı!";
            return result;
        }
        else{
            result.data = appointmentsBarber;
            return result;
        }
    }

    public GeneralDto.Response findDateandCustomerId (Date appointmentDate, long customerId) throws ResourceNotFoundException {
        List<Appointment> appointmentsCustomer = new ArrayList<>();
        GeneralDto.Response result = new GeneralDto.Response();
        Iterable<Appointment> i = appointmentRepository.findByAppointmentDateAndCustomerId(appointmentDate, customerId);
        i.forEach(appointmentsCustomer::add);
        if(appointmentsCustomer == null){
            result.Error = true;
            result.Message = "Randevu bulunamadı!";
            return result;
        }
        else {
            result.data = appointmentsCustomer;
            return result;
        }
    }

    public GeneralDto.Response findDateandStaffId (Date appointmentDate, long staffId) throws ResourceNotFoundException {
        List<Appointment> appointmentsStaff = new ArrayList<>();
        GeneralDto.Response result = new GeneralDto.Response();
        appointmentRepository.findByAppointmentDateAndStaffId(appointmentDate, staffId).forEach(appointmentsStaff::add);
        if(appointmentsStaff == null){
            result.Error = true;
            result.Message = "Randevu bulunamadı!";
            return result;
        }
        else{
            result.data = appointmentsStaff;
            return result;
        }
    }

    public GeneralDto.Response filterDateBetween(Date startDate, Date endDate) throws ResourceNotFoundException {
        List<Appointment> appointmentsFilter = new ArrayList<>();
        GeneralDto.Response result = new GeneralDto.Response();
        appointmentRepository.findByAppointmentDateBetween(startDate, endDate).forEach(appointmentsFilter::add);
        if(appointmentsFilter == null){
            result.Error = true;
            result.Message = "Randevu bulunamadı!";
            return result;
        }
        else {
            result.data = appointmentsFilter;
            return result;
        }
    }

    public GeneralDto.Response findByCustomerDateBefore(long customerId, Date date) throws ResourceNotFoundException {
        List<Appointment> appointmentsFilter = new ArrayList<>();
        appointmentRepository.findByCustomerIdAndAppointmentDateBefore(customerId, date).forEach(appointmentsFilter::add);
        GeneralDto.Response result = new GeneralDto.Response();
        if(appointmentsFilter == null){
            result.Error = true;
            result.Message = "Randevu bulunamadı!";
            return result;
        }
        else{
            result.data = appointmentsFilter;
            return  result;
        }
    }

    public GeneralDto.Response findByBarberDateBefore(long barberId, Date date) throws ResourceNotFoundException {
        List<Appointment> appointmentsFilter = new ArrayList<>();
        GeneralDto.Response result = new GeneralDto.Response();
        appointmentRepository.findByBarberIdAndAppointmentDateBefore(barberId, date).forEach(appointmentsFilter::add);
        if(appointmentsFilter == null) {
            result.Error = true;
            result.Message = "Randevu bulunamadı!";
            return result;
        }
        else {
            result.data = appointmentsFilter;
            return result;
        }
    }

    public GeneralDto.Response findByStaffDateBefore(long staffId, Date date) throws ResourceNotFoundException {
        List<Appointment> appointmentsFilter = new ArrayList<>();
        GeneralDto.Response result = new GeneralDto.Response();
        appointmentRepository.findByStaffIdAndAppointmentDateBefore(staffId, date).forEach(appointmentsFilter::add);
        if(appointmentsFilter == null) {
            result.Error = true;
            result.Message = "Randevu bulunamadı!";
            return result;
        }
        else {
            result.data = appointmentsFilter;
            return result;
        }
    }

    public GeneralDto.Response filterDateBetweenByStaffId(long staffId, Date startDate, Date endDate) throws ResourceNotFoundException {
        List<Appointment> appointmentsFilter = new ArrayList<>();
        GeneralDto.Response result = new GeneralDto.Response();
        Iterable<Appointment> i = appointmentRepository.findByStaffIdAndAppointmentDateBetween(staffId, startDate, endDate);
        i.forEach(appointmentsFilter::add);
        if(appointmentsFilter == null) {
            result.Error = true;
            result.Message = "Randevu bulunamadı!";
            return result;
        }
        else {
            result.data = appointmentsFilter;
            return result;
        }
    }

    public GeneralDto.Response filterDateBetweenByBarberId(long barberId, Date startDate, Date endDate) throws ResourceNotFoundException {
        List<Appointment> appointmentsFilter = new ArrayList<>();
        GeneralDto.Response result = new GeneralDto.Response();
        Iterable<Appointment> i = appointmentRepository.findByBarberIdAndAppointmentDateBetween(barberId, startDate, endDate);
        i.forEach(appointmentsFilter::add);
        if(appointmentsFilter == null) {
            result.Error = true;
            result.Message = "Randevu bulunamadı!";
            return result;
        }
        else {
            result.data = appointmentsFilter;
            return result;
        }
    }

    public GeneralDto.Response filterDateBetweenByCustomerId(long customerId, Date startDate, Date endDate) throws ResourceNotFoundException {
        List<Appointment> appointmentsFilter = new ArrayList<>();
        GeneralDto.Response result = new GeneralDto.Response();
        Iterable<Appointment> i = appointmentRepository.findByCustomerIdAndAppointmentDateBetween(customerId, startDate, endDate);
        i.forEach(appointmentsFilter::add);
        if(appointmentsFilter == null) {
            result.Error = true;
            result.Message = "Randevu bulunamadı!";
            return result;
        }
        else {
            result.data = appointmentsFilter;
            return result;
        }
    }

    public GeneralDto.Response findByStaffIdMonthly(long staffId) throws ResourceNotFoundException {
        List<Appointment> appointmentsFilter = new ArrayList<>();
        GeneralDto.Response result = new GeneralDto.Response();
        Date date = new Date();
        Date lastMonth = DateUtils.addMonths(date, -1);
        Iterable<Appointment> i = appointmentRepository.findByStaffIdAndAppointmentDateAfter(staffId, lastMonth);
        i.forEach(appointmentsFilter::add);
        if(appointmentsFilter == null) {
            result.Error = true;
            result.Message = "Randevu bulunamadı!";
            return result;
        }
        else {
            result.data = appointmentsFilter;
            return result;
        }
    }

    public GeneralDto.Response findByBarberIdMonthly(long barberId) throws ResourceNotFoundException {
        List<Appointment> appointmentsFilter = new ArrayList<>();
        GeneralDto.Response result = new GeneralDto.Response();
        Date date = new Date();
        Date lastMonth = DateUtils.addMonths(date, -1);
        Iterable<Appointment> i = appointmentRepository.findByBarberIdAndAppointmentDateAfter(barberId, lastMonth);
        i.forEach(appointmentsFilter::add);
        if(appointmentsFilter == null) {
            result.Error = true;
            result.Message = "Randevu bulunamadı!";
            return result;
        }
        else {
            result.data = appointmentsFilter;
            return result;
        }
    }

    public GeneralDto.Response findByCustomerIdMonthly(long customerId) throws ResourceNotFoundException {
        List<Appointment> appointmentsFilter = new ArrayList<>();
        GeneralDto.Response result = new GeneralDto.Response();
        Date date = new Date();
        Date lastMonth = DateUtils.addMonths(date, -1);
        Iterable<Appointment> i = appointmentRepository.findByCustomerIdAndAppointmentDateAfter(customerId, lastMonth);
        i.forEach(appointmentsFilter::add);
        if(appointmentsFilter == null) {
            result.Error = true;
            result.Message = "Randevu bulunamadı!";
            return result;
        }
        else {
            result.data = appointmentsFilter;
            return result;
        }
    }


    //Randevu ekleme
    public GeneralDto.Response save(Appointment appointment) throws BadResourceException, ResourceNotFoundException, ResourceAlreadyExistsException, ParseException {
        GeneralDto.Response result = new GeneralDto.Response();
        if(!StringUtils.isEmpty(appointment.getAppointmentDate()) && !StringUtils.isEmpty(appointment.getAppointmentEndDate())){
            //hoursStatusService.whenAddAppointmentUpdate(appointment);
            if(appointment.getId() != 0 && existById(appointment.getId())) {
                result.Error = true;
                result.Message = "Randevu bulunamadı!";
                return result;
            }
            boolean hourStatusControl = hoursStatusService.whenAddAppointmentUpdate(appointment);
            if(hourStatusControl == true) {
                appointmentRepository.save(appointment);
                result.data = appointment;
                return result;
            }
            else {
                result.Error = true;
                result.Message = "Çalışan randevu saatleri arasında müsait değil!";
                return result;
            }
        }
        else {
            result.Error = true;
            result.Message = "Randevu bulunamadı!";
            return result;
        }
    }
    //Randevu tarih guncelleme
    public GeneralDto.Response update(Appointment appointment) throws ResourceNotFoundException, BadResourceException {
        GeneralDto.Response result = new GeneralDto.Response();
        if(!existById(appointment.getBarberId())) {
            if(!existById( appointment.getId())) {
                result.Error = true;
                result.Message = "Randevu bulunamadı!";
                return result;
            }
            appointmentRepository.save(appointment);
            result.data = appointment;
            return result;
        }
        else {
            result.Error = true;
            result.Message = "Randevu bulunamadı!";
            return result;
        }
    }
    public GeneralDto.Response appointmentFindById(long id) throws ResourceNotFoundException {
        GeneralDto.Response result = new GeneralDto.Response();
        if(!existById(id)) {
            result.Error = true;
            result.Message = "Randevu bulunamadı!";
            return result;
        }
        else {
            result.data = appointmentRepository.AppointmentfindById(id);
            return result;
        }
    }
    //Idye gore silme
    public void deleteById(Long id) throws ResourceNotFoundException, BadResourceException, ParseException {
        if(!existById( id)) {
            throw new ResourceNotFoundException("Cannot find appointment with id: " + id);
        }
        else {
            Appointment appointmentInfos = appointmentRepository.AppointmentfindById(id);
            hoursStatusService.whenDeleteAppointmentUpdate(appointmentInfos);
            appointmentRepository.deleteById(id);
        }
    }
    //Kac adet randevu var
    public  Long count() {
        return appointmentRepository.count();
    }


}
