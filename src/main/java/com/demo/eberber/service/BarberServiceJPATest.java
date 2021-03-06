package com.demo.eberber.service;

import com.demo.eberber.domain.Barber;
import com.demo.eberber.exception.ResourceNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import javax.sql.DataSource;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.demo.eberber.repository.BarberRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BarberServiceJPATest {
    @Autowired
    private BarberService barberService;

    @Autowired
    BarberRepository barberRepository;

    @Rule
    public  ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void testSaveUpdateDeleteBarber() throws  Exception {
        Barber b = new Barber();
        b.setBarberName("Kuaför Mahmut");
        b.setAdress("Çark caddesi Vatan bilgisayar yani");
        b.setCity("Sakarya");
        b.setDistrict("Serdivan");
        b.seteMail("abc@123.com");
        b.setNeighborhood("Kemalpaşa Mahallesi");
        b.setPhoneNo("05344343211");

        barberService.save(b);
        assertNotNull(b.getId());

        Barber findBarber = barberRepository.findById(b.getId());
        assertEquals("kuaför Mahmut", findBarber.getBarberName());
        assertEquals("abc@123.com", findBarber.geteMail());

        //kayıt guncelleme
        b.seteMail("def@456.com");
        barberService.update(b);

        //guncelleme sonrasi test
        findBarber = barberRepository.findById(b.getId());
        assertEquals("def@456.com", findBarber.geteMail());

        //silme testi
        barberService.deleteById(b.getId());

        //Silmeden sonra kuyruk durumu
        exceptionRule.expect(ResourceNotFoundException.class);
        barberService.findById(b.getId());
    }
}
