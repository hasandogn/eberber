package com.demo.eberber.service;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.sql.DataSource;

import com.demo.eberber.repository.CustomerRepository;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;

import com.demo.eberber.domain.Customer;
import com.demo.eberber.exception.ResourceNotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
/*
* @RunWith(SpringRunner.class)JUnit'e Spring'in test desteğini kullanarak çalıştırmasını söyler.
* SpringRunneryeni isim SpringJUnit4ClassRunner, göze göre biraz daha kolay.
@SpringBootTest“Spring Boot desteğiyle bootstrap” diyor
* (örneğin, application.propertiestüm Spring Boot iyiliğini yükle ve bana ver)
Bu webEnvironmentözellik test için belirli “web ortamları” yapılandırılmasını sağlar.
* Bir MOCKservlet ortamıyla veya a RANDOM_PORTveya a üzerinde çalışan gerçek bir HTTP sunucusuyla testleri başlatabilirsiniz DEFINED_PORT.
Belirli bir konfigürasyon yüklemek istiyorsak, classesniteliğini kullanabiliriz @SpringBootTest.
* */
@Service
public class CustomerServiceJPATest {

    @Autowired 
    private CustomerService CustomerService;

    @Autowired
    private CustomerRepository customerRepository;
        
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();
    
    @Test
    public void testSaveUpdateDeleteCustomer() throws Exception{
        Customer c = new Customer();
        c.setName("Portgas D. Ace");
        c.setPhoneNo("09012345678");
        c.seteMail("ace@whitebeard.com");
        
        CustomerService.save(c);
        assertNotNull(c.getId());
        
        Customer findCustomer = customerRepository.findById(c.getId());
        assertEquals("Portgas D. Ace", findCustomer.getName());
        assertEquals("ace@whitebeard.com", findCustomer.geteMail());
        
        // update record
        c.seteMail("ace@whitebeardpirat.es");
        CustomerService.update(c);
        
        // test after update
        findCustomer = customerRepository.findById(c.getId());
        assertEquals("ace@whitebeardpirat.es", findCustomer.geteMail());
        
        // test delete
        CustomerService.deleteById((long) c.getId());
        
        // query after delete
        exceptionRule.expect(ResourceNotFoundException.class);
        CustomerService.findById(c.getId());
    }    
}
