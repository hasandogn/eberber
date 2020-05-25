package com.demo.eberber.service;


import com.demo.eberber.Dto.CustomerDto;
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

    @Autowired
    private MailService mailService;

    private boolean existsById(Long i) {
        return customerRepository.existsById(i);
    }
    
    public Customer findById(Long customerId) throws ResourceNotFoundException {
        Customer Customer = customerRepository.findById(customerId).orElse(null);
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
        Customer customer = customerRepository.findByeMail(Customer.geteMail());
        if(customer != null)
            throw  new ResourceAlreadyExistsException("Your e-mail address is in the system.\n");
        if (!StringUtils.isEmpty(Customer.getName())) {
            if (Customer.getId() != 0 && existsById(Long.valueOf(Customer.getId()))) {
                throw new ResourceAlreadyExistsException("Customer with id: " + Customer.getId() +
                        " already exists");
            }
            if(Customer.geteMail() == null || Customer.getPassword().length() < 6 )
                throw new ResourceAlreadyExistsException("Your information was entered incorrectly.\n");
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
    
    public void deleteById(Long id) throws ResourceNotFoundException {
        if (!existsById(id)) {
            throw new ResourceNotFoundException("Cannot find Customer with id: " + id);
        }
        else {
            customerRepository.deleteById(id );
        }
    }

    public Customer Login(String eMail, String password) throws ResourceNotFoundException{
        Customer result = customerRepository.findByeMailAndPassword(eMail, password);
        if(result != null){
            return result;
        }
        else {
            throw new ResourceNotFoundException("Cannot find customer with email: " + eMail);
        }
    }

    public String sendMailForForgotPw(String eMail){
       Customer customer = customerRepository.findByeMail(eMail);
       if(customer != null){
           mailService.sendMail(eMail);
           return "Mail adresinize şifre değiştirme maili gönderilmiştir.";
       }
       else{
           return "Mailiniz yanlış.";
       }

    }
    public String changePassword(CustomerDto.changePassword changePassword){
        Customer resultCustomer;
        resultCustomer = customerRepository.findByeMail(changePassword.eMail);
        if(resultCustomer != null) {
            if (changePassword.password == changePassword.controlPassword) {
                resultCustomer.setPassword(changePassword.password);
                customerRepository.save(resultCustomer);
                return "Şifreniz başarıyla değişti.";
            } else {
                return "Şifreniz uyuşmuyor.";
            }
        } else {
            return  "Mailiniz yanlış!";
        }
    }
    public void updatePassword(String password, String controlPassword, long id) throws ResourceNotFoundException{
        if(password == controlPassword){
            Customer customer = findById(id);
            customer.setPassword(password);
            customerRepository.save(customer);
        }
        else {
            throw new ResourceNotFoundException("Your information should not be empty.\n.");
        }
    }



}
