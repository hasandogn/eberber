package com.demo.eberber.repository;
/*
 * Depo katmanı (veya bazen DAO katmanı), 
 * kullanılan veri depolama ile iletişimden sorumludur. 
 * ContactRepository, veritabanındaki iletişim verilerine erişmek için kullanılacaktır.
 * 
 * */
import com.demo.eberber.domain.Customer;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

public interface CustomerRepository extends PagingAndSortingRepository<Customer, Long>, 
        JpaSpecificationExecutor<Customer> {

	
}