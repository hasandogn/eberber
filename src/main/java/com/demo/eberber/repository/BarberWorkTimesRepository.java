package com.demo.eberber.repository;

import com.demo.eberber.domain.BarberWorkTimes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BarberWorkTimesRepository extends JpaRepository<BarberWorkTimes, Long> {
    List<BarberWorkTimes> findAllByBarberId(long barberId);
}
