package com.demo.eberber.service;


import com.demo.eberber.domain.Address;
import com.demo.eberber.domain.Customer;
import com.demo.eberber.exception.BadResourceException;
import com.demo.eberber.exception.ResourceAlreadyExistsException;
import com.demo.eberber.exception.ResourceNotFoundException;
import com.demo.eberber.repository.CustomerRepository;
import com.demo.eberber.specification.CustomerSpecification;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
 
@Service
public class CustomerService {
    //@Component eski olan test anonuin..dir.TDD kullanmak gerekiyor.İşi zorlastiriyor.
    @Autowired
    //yapıcı enjeksiyonlarını kullanan bileşenler yazmak çok kolaydır @Autowired. Tek bir kurucunuz olduğu sürece,
    // Spring bunu otomatik olarak hedeflenen bir hedef olarak görecektir.
    private CustomerRepository customerRepository;
    
    private boolean existsById(Long i) {
        return customerRepository.existsById(i);
    }
    
    public Customer findById(Long customerId) throws ResourceNotFoundException {
        Customer Customer = customerRepository.findById(Long.valueOf(customerId)).orElse(null);
        if (Customer==null) {
            throw new ResourceNotFoundException("Cannot find Customer with id: " + customerId);
        }
        else return Customer;
    }
    
    public List<Customer> findAll(int pageNumber, int rowPerPage) {
        List<Customer> Customers = new ArrayList<>();
        customerRepository.findAll(PageRequest.of(pageNumber - 1, rowPerPage)).forEach(Customers::add);
        return Customers;
    }
    
    public List<Customer> findAllByName(String name, int pageNumber, int rowPerPage) {
        Customer filter = new Customer();
        filter.setName(name);
        Specification<Customer> spec = new CustomerSpecification(filter);
        
        List<Customer> Customers = new ArrayList<>();
        customerRepository.findAll(spec, PageRequest.of(pageNumber - 1, rowPerPage)).forEach(Customers::add);
        return Customers;
    }
    
    public Customer save(Customer Customer) throws BadResourceException, ResourceAlreadyExistsException {
        if (!StringUtils.isEmpty(Customer.getName())) {
            if (Customer.getId() != null && existsById(Long.valueOf(Customer.getId()))) { 
                throw new ResourceAlreadyExistsException("Customer with id: " + Customer.getId() +
                        " already exists");
            }
            return customerRepository.save(Customer);
        }
        else {
            BadResourceException exc = new BadResourceException();
            exc.addErrorMessage("Customer is null or empty");
            throw exc;
        }
    }
    
    public void update(Customer Customer) 
            throws BadResourceException, ResourceNotFoundException {
        if (!StringUtils.isEmpty(Customer.getName())) {
            if (!existsById(Long.valueOf(Customer.getId()))) {
                throw new ResourceNotFoundException("Cannot find Customer with id: " + Customer.getId());
            }
            customerRepository.save(Customer);
        }
        else {
            BadResourceException exc = new BadResourceException("Failed to save Customer");
            exc.addErrorMessage("Customer is null or empty");
            throw exc;
        }
    }
    
    public void updateAddress(int id, Address address) 
            throws ResourceNotFoundException {
        Customer Customer = findById(Long.valueOf(id));
        /*Customer.setAddress1(address.getAddress1());
        Customer.setAddress2(address.getAddress2());
        Customer.setAddress3(address.getAddress3());
        Customer.setPostalCode(address.getPostalCode());*/
        customerRepository.save(Customer);
    }
    
    public void deleteById(long i) throws ResourceNotFoundException {
        if (!existsById(i)) { 
            throw new ResourceNotFoundException("Cannot find Customer with id: " + i);
        }
        else {
            customerRepository.deleteById((long) i);
        }
    }
    
    public Long count() {
        return customerRepository.count();
    }

	public void updateAddress(long customerId, Address address) {
		// TODO Auto-generated method stub
		
	}


}
