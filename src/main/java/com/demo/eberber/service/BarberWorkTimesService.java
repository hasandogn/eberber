package com.demo.eberber.service;

import com.demo.eberber.domain.BarberWorkTimes;
import com.demo.eberber.repository.BarberWorkTimesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BarberWorkTimesService {
    @Autowired
    BarberWorkTimesRepository barberWorkTimesRepository;

    private String[] days = {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};

    public void saveAutho(long barberId) {
        for(int i=0; i<days.length; i++ ) {
            BarberWorkTimes newBarberWorkTimes = new BarberWorkTimes();
            newBarberWorkTimes.setBarberId(barberId);
            newBarberWorkTimes.setDay(days[i]);
            newBarberWorkTimes.setEndHour("21:00");
            newBarberWorkTimes.setStartHour("09:00");
            newBarberWorkTimes.setIsOpen("false");
            barberWorkTimesRepository.save(newBarberWorkTimes);
        }
    }

}
