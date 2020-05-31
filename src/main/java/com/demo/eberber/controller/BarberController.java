package com.demo.eberber.controller;

import com.demo.eberber.Dto.BarberDto;
import com.demo.eberber.Dto.BarberDto.updatePassword;
import com.demo.eberber.Dto.GeneralDto;
import com.demo.eberber.domain.Barber;
import com.demo.eberber.domain.Address;
import com.demo.eberber.domain.BarberWorkTimes;
import com.demo.eberber.exception.ResourceNotFoundException;
import com.demo.eberber.exception.BadResourceException;
import com.demo.eberber.exception.ResourceAlreadyExistsException;
import com.demo.eberber.service.BarberService;
import java.net.URI;
import java.net.URISyntaxException;
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
public class BarberController {
    private  final  Logger logger = LoggerFactory.getLogger(this.getClass());

    private final  int ROW_PER_PAGE = 5;

    @Autowired
    private  BarberService barberService;

    @GetMapping(value = "/barbers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeneralDto.Response> findAll (
            @RequestParam(value="page", defaultValue = "1") int pageNumber,
            @RequestParam(required = false) String name) throws ResourceNotFoundException {
        try {
            return ResponseEntity.ok(barberService.findAll(pageNumber, ROW_PER_PAGE));
        } catch (Exception ex){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

    }

    @PostMapping(value = "/barbers/name", produces = MediaType.APPLICATION_JSON_VALUE)
    public  ResponseEntity<GeneralDto.Response> findByBarberName (@RequestBody Barber barberName) {
        try {
            return ResponseEntity.ok(barberService.findAllByName(barberName.getBarberName()));
        } catch (ResourceNotFoundException ex) {
            logger.error(ex.getMessage());
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping(value = "/barbers/{barberId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public  ResponseEntity<GeneralDto.Response> findBarberById(@PathVariable long barberId) {
        try {
            return ResponseEntity.ok(barberService.findById(barberId));//donus degeri 200 json olarak
        } catch (ResourceNotFoundException ex) {
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);//donus 404 null olarak
        }
    }

    @GetMapping(value = "/barbers/workTimes/{barberId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public  ResponseEntity<GeneralDto.Response> findBarberWorkTimesById(@PathVariable long barberId) {
        try {
            return ResponseEntity.ok(barberService.findBarberWorkTimes(barberId));//donus degeri 200 json olarak
        } catch (Exception ex) {
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);//donus 404 null olarak
        }
    }

    @GetMapping(value = "/barbers/update/workTimes/{barberId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public  ResponseEntity<GeneralDto.Response> updateBarberWorkTimesById(@PathVariable long barberId, @RequestBody BarberWorkTimes barberWorkTimes) {
        try {
            barberWorkTimes.setId(barberId);
            return ResponseEntity.ok(barberService.updateBarberWorkTimes(barberWorkTimes));//donus degeri 200 json olarak
        } catch (Exception ex) {
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);//donus 404 null olarak
        }
    }


    @PostMapping(value = "/barbers/add")
    public ResponseEntity<GeneralDto.Response> addBarber(@Valid @RequestBody Barber barber)
        throws URISyntaxException {
        try {
            return ResponseEntity.ok(barberService.save(barber));
        } catch (BadResourceException | ResourceNotFoundException ex ) {
            logger.error(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();//400
        }
    }

    @PostMapping(value = "/barbers/login")
    public ResponseEntity<GeneralDto.Response> loginBarber(@Valid @RequestBody Barber loginInfo)
            throws URISyntaxException {
        try {
            return ResponseEntity.ok(barberService.Login(loginInfo.geteMail(),loginInfo.getPassword()));
        } catch (ResourceNotFoundException ex ) {
            logger.error(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();//400
        }
    }

    @PostMapping(value = "/barbers/findAddress")
    public ResponseEntity<GeneralDto.Response> findAddressBarber(@Valid @RequestBody Barber barber)
            throws URISyntaxException {
        try {
            GeneralDto.Response response = barberService.findByAddress(barber.getCity(),barber.getDistrict(), barber.getNeighborhood());
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException ex ) {
            logger.error(ex.getMessage());
            return ResponseEntity.notFound().build();
        } catch (BadResourceException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();//400
        } catch (ResourceAlreadyExistsException e) {
            logger.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/barbers/changePassword")
    public ResponseEntity<GeneralDto.Response> changePasswordBarber(@Valid @RequestBody updatePassword barberpassword)
            throws URISyntaxException {
        try {
            GeneralDto.Response response = barberService.updatePassword(barberpassword.password,barberpassword.controlPassword, barberpassword.id);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException ex ) {
            logger.error(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();//400
        }
    }

    @PutMapping("/barbers/edit/{barberId}")
    public ResponseEntity<GeneralDto.Response> updateBarber(@Valid @RequestBody Barber barber,
                                               @PathVariable long barberId) {
        try {
            barber.setId(barberId);
            GeneralDto.Response response = barberService.update(barber);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.notFound().build();
        } catch (BadResourceException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PatchMapping("/barbers/updateAddress")
    public ResponseEntity<GeneralDto.Response> updateAdress(@RequestBody Address address) {
        try {
            GeneralDto.Response response = barberService.updateAdress(address);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException ex) {
            //ilk log exception sonra 404 hatası
            logger.error(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }



    @DeleteMapping(path = "/barbers/delete/{barberId}")
    public ResponseEntity<Barber> deleteBarberById(@PathVariable long barberId) {
        try {
            barberService.deleteById(barberId);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

}
