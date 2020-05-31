package com.demo.eberber.service;

import com.demo.eberber.Dto.GeneralDto;
import com.demo.eberber.domain.ServiceBarber;
import com.demo.eberber.exception.BadResourceException;
import com.demo.eberber.exception.ResourceAlreadyExistsException;
import com.demo.eberber.exception.ResourceNotFoundException;
import com.demo.eberber.repository.ServiceBarberRepository;
import com.demo.eberber.specification.AppointmentSpecification;
import com.demo.eberber.specification.ServiceBarberSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceBarberService {
    @Autowired
    private ServiceBarberRepository serviceBarberRepository;

    private boolean existById(Long i) { return serviceBarberRepository.existsById(i);}

    public GeneralDto.Response findById(long id) throws ResourceNotFoundException {
        GeneralDto.Response result = new GeneralDto.Response();
        result.data = serviceBarberRepository.findById(id).orElse(null);
        if(result.data == null){
            result.Error = true;
            result.Message = "Hizmet bulunamadı.";
            return result;
        }
        else return result;
    }

    public GeneralDto.Response findAll(int pageNumber, int rowPerPage) throws  ResourceNotFoundException {
        GeneralDto.Response result = new GeneralDto.Response();
        result.data = serviceBarberRepository.findAll();
        if(result.data == null){
            result.Error = true;
            result.Message = "Herhangi bir hizmet türü bulunmuyor.";
            return result;
        }
        return result;
    }

    public GeneralDto.Response findAllByBarberId(int id) throws ResourceNotFoundException{
        GeneralDto.Response result = new GeneralDto.Response();
        List<ServiceBarber> appointmentsBarber = new ArrayList<>();
        Iterable<ServiceBarber> i = serviceBarberRepository.findByBarberId(id);
        i.forEach(appointmentsBarber::add);
        if(appointmentsBarber == null){
            result.Error = true;
            result.data = "Berbere ait hizmet türü bulunamadı.";
            return result;
        }
        else {
            result.data = appointmentsBarber;
            return result;
        }
    }

    public GeneralDto.Response save(ServiceBarber serviceBarber) throws BadResourceException {
        GeneralDto.Response result = new GeneralDto.Response();
        if(!StringUtils.isEmpty(serviceBarber.getPrice())){
            if(serviceBarber.getId() != 0 && existById( serviceBarber.getId())){
                result.Error = true;
                result.Message = "Hizmet türü bulunamadı.";
                return result;
            }
            result.data = serviceBarberRepository.save(serviceBarber);
            return result;
        }
        else {
            result.Error = false;
            result.Message = "Bir şeyler yanlış gitti.";
            return result;
        }
    }

    public GeneralDto.Response update(ServiceBarber serviceBarber) throws ResourceNotFoundException, BadResourceException {
        GeneralDto.Response result = new GeneralDto.Response();
        if(!existById((long) serviceBarber.getBarberId())) {
            if(!existById((long) serviceBarber.getId())){
                result.Error = true;
                result.Message = "Randevu bulunamadı.";
                return result;
            }
            ServiceBarber resultData = serviceBarberRepository.save(serviceBarber);
            result.data = resultData;
            return result;
        }
        else {
            result.Error = true;
            result.Message = "İşlem yapılırken bir sorun oluştu.";
            return result;
        }
    }


    public GeneralDto.Response deleteById(Long id) throws BadResourceException {
        GeneralDto.Response result = new GeneralDto.Response();
        if(!existById( id)) {
            result.Error = true;
            result.Message = "Hizmet bulunamadı zaten.";
            return result;
        }
        else {
            serviceBarberRepository.deleteById(id);
            result.data = "Hizmet silindi";
            return result;
        }
    }

}
