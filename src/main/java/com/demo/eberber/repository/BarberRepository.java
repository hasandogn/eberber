package com.demo.eberber.repository;

import com.demo.eberber.domain.Barber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface BarberRepository extends JpaRepository<Barber, Long>,JpaSpecificationExecutor{
    Barber findByeMail(String eMail);
    List<Barber> findByDistrict(String district);
    List<Barber> findByCity(String city);
    List<Barber> findByNeighborhood(String neighborhood);
    List<Barber> findByNeighborhoodAndCity(String neighborhood, String city);
    List<Barber> findByNeighborhoodAndDistrict(String neighborhood, String district);
    List<Barber> findByDistrictAndCity(String district, String city);
    List<Barber> findByDistrictAndNeighborhood(String district, String neighborhood);
    List<Barber> findByCityAndDistrict(String city, String district);
    List<Barber> findByCityAndNeighborhood(String city, String neighborhood);
    List<Barber> findByCityAndNeighborhoodAndDistrict(String city, String neighborhood, String district);
   /* Barber findEmailBy(String eMail);
    @Override
    List<Barber> findAllById(Iterable<Long> longs);*/
}
