package com.demo.eberber.Dto;

import java.util.Date;

public class AppointmentDto {
    public class Detail{
        public int id;
    }

    public class AppointmentDate{
        public Date dateAppointment;
        public Date hour;
    }

    public class AppointmentWithBarber{
        public long barberId;
        public Date date;
    }

    public class AppointmentWithCustomer {
        public int barberId;
        public Date date;
    }
}
