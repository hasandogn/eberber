package com.demo.eberber.controller;

import com.demo.eberber.domain.Barber;
import com.demo.eberber.domain.Address;
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
    public ResponseEntity<List<Barber>> findAll (
            @RequestParam(value="page", defaultValue = "1") int pageNumber,
            @RequestParam(required = false) String name) {
        if (StringUtils.isEmpty(name)) {
            return ResponseEntity.ok(barberService.findAll(pageNumber, ROW_PER_PAGE));
        }
        else {
            return ResponseEntity.ok(barberService.findAllByName(name, pageNumber, ROW_PER_PAGE));
        }
    }

    @GetMapping(value = "/barbers/{barberId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public  ResponseEntity<Barber> findBarberById(@PathVariable long barberId) {
        try {
            Barber ber = barberService.findById(barberId);
            return ResponseEntity.ok(ber);//donus degeri 200 json olarak
        } catch (ResourceNotFoundException ex) {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);//donus 404 null olarak
        }
    }

    @PostMapping(value = "/barbers")
    public ResponseEntity<Barber> addBarber(@Valid @RequestBody Barber barber)
        throws URISyntaxException {
        try {
            Barber newBarber = barberService.save(barber);
            return ResponseEntity.created(new URI("/barbers/" + newBarber.getId())).body(barber);
        } catch (ResourceAlreadyExistsException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();//409
        } catch (BadResourceException | ResourceNotFoundException ex ) {
            logger.error(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();//400
        }
    }

    @PutMapping("/barbers/{barberId}")
    public ResponseEntity<Barber> updateBarber(@Valid @RequestBody Barber barber,
                                               @PathVariable long barberId) {
        try {
            barber.setId(barberId);
            barberService.update(barber);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.notFound().build();
        } catch (BadResourceException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PatchMapping("/barbers/{barberId}")
    public ResponseEntity<Void> updateAdress(@PathVariable long barberId, @RequestBody Address address) {
        try {
            barberService.updateAdress(barberId, address);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException ex) {
            //ilk log exception sonra 404 hatası
            logger.error(ex.getMessage());
            return  ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(path = "/barbers/{barberId}")
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
