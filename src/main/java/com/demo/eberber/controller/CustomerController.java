package com.demo.eberber.controller;

import com.demo.eberber.Dto.BarberDto;
import com.demo.eberber.Dto.CustomerDto;
import com.demo.eberber.domain.Address;
import com.demo.eberber.domain.Customer;
import com.demo.eberber.exception.BadResourceException;
import com.demo.eberber.exception.ResourceAlreadyExistsException;
import com.demo.eberber.exception.ResourceNotFoundException;
import com.demo.eberber.service.CustomerService;
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
public class CustomerController {
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    private final int ROW_PER_PAGE = 5;
    
    @Autowired
    private CustomerService CustomerService;
    
    @GetMapping(value = "/Customers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Customer>> findAll(
            @RequestParam(value="page", defaultValue="1") int pageNumber,
            @RequestParam(required=false) String name) {
        if (StringUtils.isEmpty(name)) {
            return ResponseEntity.ok(CustomerService.findAll(pageNumber, ROW_PER_PAGE));
        }
        else {
            return ResponseEntity.ok(CustomerService.findAllByName(name, pageNumber, ROW_PER_PAGE));
        }
    }

    //@PostMapping(value = "/Customers/getCustomer")
    @GetMapping(value = "/Customers/getCustomer/{CustomerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Customer> findCustomerById(@PathVariable long CustomerId) {
        try {
            Customer customer = CustomerService.findById(CustomerId);
            return ResponseEntity.ok(customer);  // return 200, with json body
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // return 404, with null body
        }
    }

    @PostMapping(value = "/Customers/add")
    public ResponseEntity<Customer> addCustomer(@Valid @RequestBody Customer Customer)
            throws URISyntaxException {
        try {
            Customer newCustomer = CustomerService.save(Customer);
            return ResponseEntity.created(new URI("/api/Customers/" + newCustomer.getId()))
                    .body(Customer);
        } catch (ResourceAlreadyExistsException ex) {
            // log exception first, then return Conflict (409)
            logger.error(ex.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (BadResourceException ex) {
            // log exception first, then return Bad Request (400)
            logger.error(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @PutMapping(value = "/Customers/edit/{CustomerId}")
    public ResponseEntity<Customer> updateCustomer(@Valid @RequestBody Customer Customer, 
            @PathVariable int CustomerId) {
        try {
            Customer.setId(CustomerId);
            CustomerService.update(Customer);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException ex) {
            // log exception first, then return Not Found (404)
            logger.error(ex.getMessage());
            return ResponseEntity.notFound().build();
        } catch (BadResourceException ex) {
            // log exception first, then return Bad Request (400)
            logger.error(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @PatchMapping("/Customers/update/{CustomerId}")
    public ResponseEntity<Void> updateAddress(@PathVariable long CustomerId,
            @RequestBody Address address) {
        /*try {
            CustomerService.updateAddress(CustomerId, address);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException ex) {
            // log exception first, then return Not Found (404)
            logger.error(ex.getMessage());
            return ResponseEntity.notFound().build();
        }*/
    	CustomerService.updateAddress(CustomerId, address);
 		return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/Customers/login")
    public ResponseEntity<Customer> loginCustomer(@Valid @RequestBody Customer customer)
            throws URISyntaxException {
        try {
            CustomerService.Login(customer.geteMail(),customer.getPassword());
            return ResponseEntity.created(new URI("/barbers/login/" + customer.getId())).body(customer);
        } catch (ResourceNotFoundException ex ) {
            logger.error(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();//400
        }
    }

    @PostMapping(value = "/Customers/changePassword")
    public ResponseEntity<CustomerDto.updatePassword> changePasswordBarber(@Valid @RequestBody CustomerDto.updatePassword customerPassword)
            throws URISyntaxException {
        try {
            CustomerService.updatePassword(customerPassword.password,customerPassword.controlPassword, customerPassword.id);
            return ResponseEntity.created(new URI("/barbers/changePassword/" + customerPassword.id)).body(customerPassword);
        } catch (ResourceNotFoundException ex ) {
            logger.error(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();//400
        }
    }

    @DeleteMapping(path="/Customers/delete/{CustomerId}")
    public ResponseEntity<Void> deleteCustomerById(@PathVariable int CustomerId) {
        try {
            CustomerService.deleteById((long) CustomerId);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
