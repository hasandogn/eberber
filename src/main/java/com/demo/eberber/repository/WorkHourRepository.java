package com.demo.eberber.repository;

import com.demo.eberber.domain.WorkHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WorkHourRepository  extends JpaRepository<WorkHours,Long> {
    List<WorkHours> findByBarberId(long barberId);
    List<WorkHours> findByStaffId(long staffId);
    List<WorkHours> findByDay(String day);
    List<WorkHours> findByDayAndBarberId(String day, long barberId);
    List<WorkHours> findByDayAndStaffId(String day, long staffId);
    @Query(value = "select id from work_hours wh where wh.staff_id=:staffId", nativeQuery = true)
    List<Long> findWithStaffId(@Param("staffId") long staffId);
}
