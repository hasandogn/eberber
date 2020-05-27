package com.demo.eberber.repository;
/*
 * Depo katmanı (veya bazen DAO katmanı), 
 * kullanılan veri depolama ile iletişimden sorumludur. 
 * CustomerRepository, veritabanındaki iletişim verilerine erişmek için kullanılacaktır.
 * 
 * */
import com.demo.eberber.domain.Customer;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

public interface CustomerRepository extends PagingAndSortingRepository<Customer, Long>, 
        JpaSpecificationExecutor<Customer> {
    Customer findById(int Id);
    Customer findByeMailAndPassword(String eMail, String password);
    Customer findByeMail(String eMail);

	
}