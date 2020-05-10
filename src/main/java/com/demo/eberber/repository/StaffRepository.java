package com.demo.eberber.repository;

import com.demo.eberber.domain.Staff;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface StaffRepository extends PagingAndSortingRepository<Staff, Long>, JpaSpecificationExecutor{
    List<Staff> findByBarberId(@Param("barberId")int barberId);
}
