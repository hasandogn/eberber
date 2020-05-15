package com.demo.eberber.service;

import com.demo.eberber.domain.Address;
import com.demo.eberber.domain.Barber;
import com.demo.eberber.exception.BadResourceException;
import com.demo.eberber.exception.ResourceAlreadyExistsException;
import com.demo.eberber.exception.ResourceNotFoundException;
import com.demo.eberber.repository.BarberRepository;
import com.demo.eberber.specification.BarberSpecification;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class BarberService {
    @Autowired
    private BarberRepository barberRepository;

    private boolean existById(Long i ) {
        return barberRepository.existsById(i);
    }
    //barber id yee göre bulmak
    public Barber findById(Long id ) throws ResourceNotFoundException {
        Barber barber = barberRepository.findById(id).orElse(null);
        if(barber==null) {
            throw new ResourceNotFoundException("Cannot find Barber with id : " +id);
        }
        else return barber;
    }
    //barberleri listelemek
    public List<Barber> findAll(int pageNumber, int rowPerPage) {
        List<Barber> barbers = new ArrayList<>();
        Iterable<Barber> i=barberRepository.findAll();
        i.forEach(barbers::add);
       // barberRepository.findAll(PageRequest.of(pageNumber-1, rowPerPage)).forEach(barbers::add);
        return barbers;
    }
    //Isime gore tum barberleri bulmak
    public List<Barber> findAllByName(String name, int pageNumber, int rowPerPage ) {
        Barber filter = new Barber();
        filter.setBarberName(name);
        Specification<Barber> spec = new BarberSpecification(filter);

        List<Barber> barbers = new ArrayList<>();
        Iterable<Barber> i=barberRepository.findAll();
        i.forEach(barbers::add);
        return barbers;
    }

    public List<Barber> findByAddress(String city, String district, String neighborhood ) throws BadResourceException, ResourceAlreadyExistsException, ResourceNotFoundException {
        List<Barber> barbers = new ArrayList<>();
        if((StringUtils.isEmpty(city) && StringUtils.isEmpty(city) && StringUtils.isEmpty(city)) || barbers == null){
            Iterable<Barber> i=barberRepository.findAll();
            i.forEach(barbers::add);
            return barbers;
        }
        if(!StringUtils.isEmpty(city)){
            if(!StringUtils.isEmpty(district)){
                if(!StringUtils.isEmpty(neighborhood)){
                    Iterable<Barber> i=barberRepository.findByCityAndNeighborhoodAndDistrict(city, neighborhood, district);
                    i.forEach(barbers::add);
                    return barbers;
                }
                else {
                    Iterable<Barber> i=barberRepository.findByCityAndDistrict(city, district);
                    i.forEach(barbers::add);
                    return barbers;
                }
            }
            else if(!StringUtils.isEmpty(neighborhood)){
                Iterable<Barber> i=barberRepository.findByCityAndNeighborhood(city, neighborhood);
                i.forEach(barbers::add);
                return barbers;
            }
            else {
                Iterable<Barber> i=barberRepository.findByCity(city);
                i.forEach(barbers::add);
                return barbers;
            }
        }
        if(!StringUtils.isEmpty(district)){
            if(!StringUtils.isEmpty(neighborhood)){
                Iterable<Barber> i=barberRepository.findByDistrictAndNeighborhood(district, neighborhood);
                i.forEach(barbers::add);
                return barbers;
            }
            else {
                Iterable<Barber> i=barberRepository.findByDistrict(district);
                i.forEach(barbers::add);
                return barbers;
            }
        }
        if(!StringUtils.isEmpty(neighborhood)){
            Iterable<Barber> i=barberRepository.findByNeighborhood(neighborhood);
            i.forEach(barbers::add);
            return barbers;
        }
        else{
            BadResourceException exc = new BadResourceException("Failed to find address");
            exc.addErrorMessage("Barber is null empty");
            throw exc;
        }
    }

    //Ekleme servisi
    public Barber save(Barber barber) throws BadResourceException, ResourceNotFoundException {
        Barber controlBarber = barberRepository.findByeMail(barber.geteMail());
        if(controlBarber != null)
            throw  new ResourceNotFoundException("Your e-mail address is in the system.\n");
        if(!StringUtils.isEmpty(barber.getBarberName())){
            if(barber.getId() != null && existById(barber.getId())){
                throw  new ResourceNotFoundException("Barber with id: "+ barber.getId() + " already exists");
            }
            if( barber.getPassword().length() < 6 )
                throw  new ResourceNotFoundException("There is something wrong with your information.\n");
            return barberRepository.save(barber);
        }
        else {
            BadResourceException exc = new BadResourceException("Failed to save barber");
            exc.addErrorMessage("Barber is null empty");
            throw exc;
        }
    }
    //Guncelleme servisi
    public void update(Barber barber) throws BadResourceException, ResourceNotFoundException {
            if(!StringUtils.isEmpty(barber.getBarberName())) {
                if(!existById(barber.getId())) {
                    throw new ResourceNotFoundException("Barber find Contact with id: " + barber.getId());
                }
                barberRepository.save(barber);
            }
            else {
                BadResourceException exc = new BadResourceException("Failed to save barber");
                exc.addErrorMessage("Barber is null or empty");
                throw exc;
            }
    }
    //Adres guncellemesi
    public void updateAdress(Address adress) throws ResourceNotFoundException{
        if(adress == null )
            throw new ResourceNotFoundException("Your information should not be empty.\n.");
        else {
            Barber barber = findById(adress.getBarberId());
            barber.setAdress(adress.getAddressDetail());
            barber.setCity(adress.getCity());
            barber.setDistrict(adress.getDistrict());
            barber.setNeighborhood(adress.getNeighborhood());

            barberRepository.save(barber);
        }
    }

    public Barber Login(String eMail, String password) throws ResourceNotFoundException{

        if(eMail != null){
            Barber barber = barberRepository.findByeMail(eMail);
            if(barber == null)
                throw new ResourceNotFoundException("Cannot find barber with email: ");
            if(barber.getPassword() != password)
                throw new ResourceNotFoundException("You have entered the password incorrectly.\n");
            else
                return barber;
        }
        else {
            throw new ResourceNotFoundException("Cannot find barber with email: " + eMail);
        }
    }

    public void updatePassword(String password, String controlPassword, long id) throws ResourceNotFoundException{
        if(password == controlPassword){
            Barber barber = findById(id);
            barber.setPassword(password);
            barberRepository.save(barber);
        }
        else {
            throw new ResourceNotFoundException("Your information should not be empty.\n.");
        }
    }

    //Id e gore silme islemi
    public void deleteById(Long id) throws ResourceNotFoundException {
        if(!existById(id)) {
            throw new ResourceNotFoundException("Cannot find barber with id: " + id);
        }
        else {
            barberRepository.deleteById(id);
        }
    }
    //Kac adet barber var
    public Long count() {
        return barberRepository.count();
    }
}

