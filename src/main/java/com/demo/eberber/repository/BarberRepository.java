package com.demo.eberber.repository;

import com.demo.eberber.domain.Barber;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


public interface BarberRepository extends PagingAndSortingRepository<Barber, Long>,JpaSpecificationExecutor{
}
