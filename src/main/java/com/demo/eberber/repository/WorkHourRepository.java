package com.demo.eberber.repository;

import com.demo.eberber.domain.WorkHours;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkHourRepository  extends JpaRepository<WorkHours,Long> {
    List<WorkHours> findByBarberId(long barberId);
    List<WorkHours> findByStaffId(long staffId);
    List<WorkHours> findByDay(String day);
    List<WorkHours> findByDayAndBarberId(String day, long barberId);
    List<WorkHours> findByDayAndStaffId(String day, long staffId);
}
