package com.demo.eberber.service;

import com.demo.eberber.Dto.GeneralDto;
import com.demo.eberber.domain.Address;
import com.demo.eberber.domain.Barber;
import com.demo.eberber.domain.BarberWorkTimes;
import com.demo.eberber.exception.BadResourceException;
import com.demo.eberber.exception.ResourceAlreadyExistsException;
import com.demo.eberber.exception.ResourceNotFoundException;
import com.demo.eberber.repository.BarberRepository;
import com.demo.eberber.repository.BarberWorkTimesRepository;
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
    @Autowired
    private BarberWorkTimesRepository barberWorkTimesRepository;
    @Autowired
    private BarberWorkTimesService barberWorkTimesService;

    private boolean existById(Long i ) {
        return barberRepository.existsById(i);
    }
    //barber id yee göre bulmak
    public GeneralDto.Response findById(Long id ) throws ResourceNotFoundException {
        GeneralDto.Response result = new GeneralDto.Response();
        result.data = barberRepository.findById(id).orElse(null);
        if(result.data == null) {
            result.Error = true;
            result.Message = "Berber bulunamadı! Lütfen tekrar deneyin.";
            return result;
        }
        else return result;
    }
    //barberleri listelemek
    public GeneralDto.Response findAll(int pageNumber, int rowPerPage) {
        GeneralDto.Response result = new GeneralDto.Response();
        List<Barber> barbers = new ArrayList<>();
        Iterable<Barber> i=barberRepository.findAll();
        i.forEach(barbers::add);
        result.data = barbers;
        return result;
    }


    //Isime gore tum barberleri bulmak
    public GeneralDto.Response findAllByName(String name) throws ResourceNotFoundException {
        List<Barber> barbers = new ArrayList<>();
        GeneralDto.Response result = null;
        if(name != null) {
            Iterable<Barber> i = barberRepository.findAllByBarberName(name);
            i.forEach(barbers::add);
            result.data = barbers;
            return result;
        }
        else {
            Iterable<Barber> i = barberRepository.findAll();
            i.forEach(barbers::add);
            result.data = barbers;
            return result;
        }
    }

    public GeneralDto.Response findByAddress(String city, String district, String neighborhood ) throws BadResourceException, ResourceAlreadyExistsException, ResourceNotFoundException {
        List<Barber> barbers = new ArrayList<>();
        GeneralDto.Response result = new GeneralDto.Response();
        if((StringUtils.isEmpty(city) && StringUtils.isEmpty(district) && StringUtils.isEmpty(neighborhood)) || barbers == null){
            Iterable<Barber> i=barberRepository.findAll();
            i.forEach(barbers::add);
            result.data = barbers;
            return result;
        }
        if(!StringUtils.isEmpty(city)){
            if(!StringUtils.isEmpty(district)){
                if(!StringUtils.isEmpty(neighborhood)){
                    Iterable<Barber> i=barberRepository.findByCityAndNeighborhoodAndDistrict(city, neighborhood, district);
                    i.forEach(barbers::add);
                    result.data = barbers;
                    return result;
                }
                else {
                    Iterable<Barber> i=barberRepository.findByCityAndDistrict(city, district);
                    i.forEach(barbers::add);
                    result.data = barbers;
                    return result;
                }
            }
            else if(!StringUtils.isEmpty(neighborhood)){
                Iterable<Barber> i=barberRepository.findByCityAndNeighborhood(city, neighborhood);
                i.forEach(barbers::add);
                result.data = barbers;
                return result;
            }
            else {
                Iterable<Barber> i=barberRepository.findByCity(city);
                i.forEach(barbers::add);
                result.data = barbers;
                return result;
            }
        }
        if(!StringUtils.isEmpty(district)){
            if(!StringUtils.isEmpty(neighborhood)){
                Iterable<Barber> i=barberRepository.findByDistrictAndNeighborhood(district, neighborhood);
                i.forEach(barbers::add);
                result.data = barbers;
                return result;
            }
            else {
                Iterable<Barber> i=barberRepository.findByDistrict(district);
                i.forEach(barbers::add);
                result.data = barbers;
                return result;
            }
        }
        if(!StringUtils.isEmpty(neighborhood)){
            Iterable<Barber> i=barberRepository.findByNeighborhood(neighborhood);
            i.forEach(barbers::add);
            result.data = barbers;
            return result;
        }
        else{
            Iterable<Barber> i=barberRepository.findAll();
            i.forEach(barbers::add);
            result.data = barbers;
            return result;
        }
    }

    //Ekleme servisi
    public GeneralDto.Response save(Barber barber) throws BadResourceException, ResourceNotFoundException {
        Barber controlBarber = barberRepository.findByeMail(barber.geteMail());
        GeneralDto.Response result = new GeneralDto.Response();
        if(controlBarber != null){
            result.Error = true;
            result.Message = "Bu maile ait kullanıcı bulunuyor!";
            return result;
        }
        if(!StringUtils.isEmpty(barber.getBarberName())){
            if(existById(barber.getId())){
                result.Error = true;
                result.Message = "Berber bulunamadı! Lütfen tekrar deneyin.";
                return result;
            }
            if( barber.getPassword().length() < 6 ){
                result.Error = true;
                result.Message = "Şifreniz 6 haneden küçük olmamalıdır!";
                return result;
            }
            Barber newBarber = barberRepository.save(barber);
            barberWorkTimesService.saveAutho(newBarber.getId());
            result.data = newBarber;
            return result;
        }
        else {
            result.Error = true;
            result.Message = "Berber kaydedilirken hata oluştu! Lütfen tekrar deneyin.";
            return result;
        }
    }
    //Guncelleme servisi
    public GeneralDto.Response update(Barber barber) throws BadResourceException, ResourceNotFoundException {
        GeneralDto.Response result = new GeneralDto.Response();
            if(!StringUtils.isEmpty(barber.getBarberName())) {
                if(!existById(barber.getId())) {
                    result.Error = true;
                    result.Message = "Berber bulunamadı! Lütfen tekrar deneyin.";
                    return result;
                }
                result.data = barberRepository.save(barber);
                return result;
            }
            else {
                result.Error = true;
                result.Message = "Güncelleme yaparken hata oluştu! Lütfen tekrar deneyin.";
                return result;
            }
    }
    //Adres guncellemesi
    public GeneralDto.Response updateAdress(Address adress) throws ResourceNotFoundException{
        GeneralDto.Response result = new GeneralDto.Response();
        if(adress == null ){
            result.Error = true;
            result.Message = "Düzenlenecek adres bulunamadı! Lütfen tekrar deneyin.";
            return result;
        }
        else {
            Barber barber = new Barber();
            barber = barberRepository.findById(adress.getBarberId());
            barber.setAdress(adress.getAddressDetail());
            barber.setCity(adress.getCity());
            barber.setDistrict(adress.getDistrict());
            barber.setNeighborhood(adress.getNeighborhood());

            barberRepository.save(barber);
            result.data = barber;
            return result;
        }
    }

    public GeneralDto.Response Login(String eMail, String password) throws ResourceNotFoundException{
        GeneralDto.Response result = new GeneralDto.Response();
        if(eMail != null){
            Barber barber = barberRepository.findByeMail(eMail);
            if(barber == null){
                result.Error = true;
                result.Message = "Böyle bir maile ait kayıt bulunmuyor!";
                return result;
            }
            if(!barber.getPassword().equals(password)){
                result.Error = true;
                result.Message = "Yanlış şifre girdiniz!";
                return result;
            }
            else{
                result.data = barber;
                return result;
            }
        }
        else {
            result.Error = true;
            result.Message = "Bir hata oluştu! Lütfen tekrar deneyin.";
            return result;
        }
    }

    public GeneralDto.Response updatePassword(String password, String controlPassword, long id) throws ResourceNotFoundException{
        GeneralDto.Response result = new GeneralDto.Response();
        if(password == controlPassword){
            Barber barber = barberRepository.findById(id);
            barber.setPassword(password);
            barberRepository.save(barber);
            result.data = barber;
            return result;
        }
        else {
            result.Error = true;
            result.Message = "Bir hata oluştu! Lütfen tekrar deneyin.";
            return result;
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

    public GeneralDto.Response findBarberWorkTimes(long id){
        GeneralDto.Response result = new GeneralDto.Response();
        List<BarberWorkTimes> barberWorkTimes = barberWorkTimesRepository.findAllByBarberId(id);
        result.data = barberWorkTimes;
        return  result;
    }

    public GeneralDto.Response updateBarberWorkTimes(BarberWorkTimes barberWorkTimes){
        BarberWorkTimes barberWorkTimes1 = new BarberWorkTimes();
        barberWorkTimes1 = barberWorkTimesService.update(barberWorkTimes);
        GeneralDto.Response result = new GeneralDto.Response();
        result.data = barberWorkTimes1;
        return result;
    }
    //Kac adet barber var
    public Long count() {
        return barberRepository.count();
    }
}

