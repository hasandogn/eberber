package com.demo.eberber.service;


import com.demo.eberber.Dto.CustomerDto;
import com.demo.eberber.Dto.GeneralDto;
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
    
    public GeneralDto.Response findById(int id) throws ResourceNotFoundException {
        GeneralDto.Response result = new GeneralDto.Response();
        Customer Customer = customerRepository.findById(id);
        if (Customer==null) {
            result.Error = true;
            result.Message = "Müşteri bulunamadı!";
            return result;
        }
        else {
            result.data = Customer;
            return result;
        }
    }

    public GeneralDto.Response findAll(int pageNumber, int rowPerPage) {
        GeneralDto.Response result = new GeneralDto.Response();
        List<Customer> Customers = new ArrayList<>();
        customerRepository.findAll().forEach(Customers::add);
        result.data = Customers;
        return result;
    }
    
    public GeneralDto.Response findAllByName(String name, int pageNumber, int rowPerPage) {
        GeneralDto.Response result = new GeneralDto.Response();
        Customer filter = new Customer();
        filter.setName(name);
        Specification<Customer> spec = new CustomerSpecification(filter);
        
        List<Customer> Customers = new ArrayList<>();
        customerRepository.findAll(spec, PageRequest.of(pageNumber - 1, rowPerPage)).forEach(Customers::add);
        result.data = Customers;
        return result;
    }
    
    public GeneralDto.Response save(Customer Customer) throws BadResourceException, ResourceAlreadyExistsException {
        Customer customer = new Customer();
        customer = customerRepository.findByeMail(Customer.geteMail());
        GeneralDto.Response result = new GeneralDto.Response();
        if(customer != null){
            result.Error = true;
            result.Message = "Bu mail adresine ait hesap var!";
            return result;
        }
        if (!StringUtils.isEmpty(Customer.getName())) {
            if (Customer.getId() != 0 && existsById(Long.valueOf(Customer.getId()))) {
                result.Error = true;
                result.Message = "Bir hata oluştu! Lütfen tekrar deneyin.";
                return result;
            }
            if(Customer.geteMail() == null || Customer.getPassword().length() < 6 ) {
                result.Error = true;
                result.Message = "Şifreniz belirtilen kriterlere uymuyor!";
                return result;
            }
            customerRepository.save(Customer);
            result.data = Customer;

            return result;
        }
        else {
            result.Error = true;
            result.Message = "Bir hata oluştu! Lütfen tekrar deneyin.";
            return result;
        }
    }
    
    public GeneralDto.Response update(Customer Customer)
            throws BadResourceException, ResourceNotFoundException {
        GeneralDto.Response result = new GeneralDto.Response();
        if (!StringUtils.isEmpty(Customer.getName())) {
            if (Customer.geteMail() == null) {
                result.Error = true;
                result.Message = "Bir hata oluştu! Lütfen tekrar deneyin.";
                return result;
            }
            result.data = customerRepository.save(Customer);
            return result;
        }
        else {
            result.Error = true;
            result.Message = "Bir hata oluştu! Lütfen tekrar deneyin.";
            return result;
        }
    }
    
    public GeneralDto.Response updateAddress(int id, Address address)
            throws ResourceNotFoundException {
        GeneralDto.Response result = new GeneralDto.Response();
        try {
            Customer customer = customerRepository.findById(id);
            customer.setAddress(address.getAddressDetail());
            customer.setUserCity(address.getCity());
            customer.setUserDistrict(address.getDistrict());
            customer.setUserNeighborhood(address.getNeighborhood());
            customerRepository.save(customer);
            result.data = customer;
            return result;
        } catch (Exception ex) {
            result.Error = true;
            result.Message = "Bir hata oluştu! Lütfen tekrar deneyin.";
            return result;
        }

    }
    
    public void deleteById(Long id) throws ResourceNotFoundException {
        if (!existsById(id)) {
            throw new ResourceNotFoundException("Cannot find Customer with id: " + id);
        }
        else {
            customerRepository.deleteById(id );
        }
    }

    public GeneralDto.Response Login(String eMail, String password) throws ResourceNotFoundException{
        Customer customer = customerRepository.findByeMailAndPassword(eMail, password);
        GeneralDto.Response result = new GeneralDto.Response();
        if(customer != null){
            result.data = customer;
            return result;
        }
        else {
            result.Error = true;
            result.Message = "Bu mail adresi kayıtlı değil! Lütfen kayıt olun.";
            return result;
        }
    }

    public GeneralDto.Response sendMailForForgotPw(String eMail){
        GeneralDto.Response result = new GeneralDto.Response();
        Customer customer = customerRepository.findByeMail(eMail);
        if(customer != null){
           mailService.sendMail(eMail);
           result.data = eMail;
           return result;
        }
        else{
            result.Error = true;
            result.Message = "Bu mail adresi kayıtlı değil!";
            return result;
        }

    }
    public GeneralDto.Response changePassword(CustomerDto.changePassword changePassword){
        GeneralDto.Response result = new GeneralDto.Response();
        Customer resultCustomer = new Customer();
        resultCustomer = customerRepository.findByeMail(changePassword.eMail);
        if(resultCustomer != null) {
            if (changePassword.password == changePassword.controlPassword) {
                resultCustomer.setPassword(changePassword.password);
                customerRepository.save(resultCustomer);
                result.data = resultCustomer;
                return result;
            } else {
                result.Error = true;
                result.Message = "Şifreni uyuşmuyor!";
                return result;
            }
        } else {
            result.Error = true;
            result.Message = "Böyle bir mail kayıtlı değil!";
            return result;
        }
    }
    public GeneralDto.Response updatePassword(String password, String controlPassword, long id) throws ResourceNotFoundException{
        GeneralDto.Response result = new GeneralDto.Response();
        if(password == controlPassword){
            Customer customer = customerRepository.findById((int) id);
            customer.setPassword(password);
            customerRepository.save(customer);
            result.data = customer;
            return result;
        }
        else {
            result.Error = true;
            result.Message = "Bir hata oluştu! Lütfen tekrar deneyin.";
            return result;
        }
    }



}
