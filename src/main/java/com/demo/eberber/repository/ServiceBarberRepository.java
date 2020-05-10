package com.demo.eberber.repository;

import com.demo.eberber.domain.ServiceBarber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceBarberRepository extends JpaRepository<ServiceBarber, Long> {
    List<ServiceBarber> findByBarberId(int id);
}
