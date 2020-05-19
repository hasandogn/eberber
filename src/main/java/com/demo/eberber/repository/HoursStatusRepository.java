package com.demo.eberber.repository;

import com.demo.eberber.Dto.HourStatusDto;
import com.demo.eberber.domain.HoursStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HoursStatusRepository extends JpaRepository<HoursStatus, Long> {
    List<HoursStatus> findByStaffId(long staffId);
    //List<HoursStatus> findByStaffIdAndDayAndEmpty(long staffId, String day);
    //List<HoursStatus> findByStaffIdAndEmpty(long staffId, boolean empty);
    @Query(value="select hour from hours_status hs where hs.staff_id =:staffId and hs.day =:day and hs.empty_is_hours=:emptyIsHours order by hour asc", nativeQuery=true)
    List<String> findByStaffIdAndDayAndEmptyIsHoursOrderByHourAsc(@Param("staffId") long staffId, @Param("day") String day, @Param("emptyIsHours") String emptyIsHours);
    @Query(value="select day from hours_status hs where hs.staff_id =:staffId ", nativeQuery=true)
    List<String> findByStaffIdOrderByDay(long staffId);
    @Query(value = "select distinct day from hours_status hs where hs.staff_id=:staffId", nativeQuery = true)
    List<String> findDistinctByStaffId(@Param("staffId") long staffId);
    HoursStatus findByStaffIdAndDayAndHour(long staffId, String day, String hour);
    @Query(value="select empty_is_hours from hours_status hs where hs.staff_id =:staffId and hs.day =:day and hs.hour=:hour", nativeQuery=true)
    String findWithStaffIdAndDayAndHour(@Param("staffId") long staffId, @Param("day") String day,@Param("hour") String hour);

}
