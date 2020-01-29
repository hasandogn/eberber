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

    private boolean existById(Long i ) { return barberRepository.existsById(i);}
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

    //Ekleme servisi
    public Barber save(Barber barber) throws BadResourceException, ResourceAlreadyExistsException, ResourceNotFoundException {
        if(!StringUtils.isEmpty(barber.getBarberName())){
            if(barber.getId() != null && existById(barber.getId())){
                throw  new ResourceNotFoundException("Barber with id: "+ barber.getId() + " already exists");

            }
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
    public void updateAdress(Long id, Address adress) throws ResourceNotFoundException{
        Barber barber = findById(id);
        barber.setAdress(barber.getAdress());
        barberRepository.save(barber);
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

