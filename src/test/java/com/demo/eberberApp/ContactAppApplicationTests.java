package com.demo.eberberApp;

import com.demo.eberber.domain.Customer;
import com.demo.eberber.exception.ResourceNotFoundException;
import com.demo.eberber.service.CustomerService;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SpringBootTest
class CustomerAppApplicationTests {

	/**
	 *
	 */
	@Autowired
	private CustomerService CustomerService;

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

		Customer findCustomer = CustomerService.findById(Long.valueOf(c.getId()));
		assertEquals("Portgas D. Ace", findCustomer.getName());
		assertEquals("ace@whitebeard.com", findCustomer.geteMail());

		// update record
		c.seteMail("ace@whitebeardpirat.es");
		CustomerService.update(c);

		// test after update
		findCustomer = CustomerService.findById(Long.valueOf(c.getId()));
		assertEquals("ace@whitebeardpirat.es", findCustomer.geteMail());

		// test delete
		CustomerService.deleteById((long) c.getId());

		// query after delete
		exceptionRule.expect(ResourceNotFoundException.class);
		CustomerService.findById(Long.valueOf(c.getId()));
	}

}
