package com.demo.eberber.Dto;

public class BarberDto {
    public class Detail {
        public int id;
    }

    public class Login {
        public String eMail;
        public String password;
    }

    public class BarberAddress {
        public String address;
        public String district;
        public String neighborhood;
        public String city;
    }

    public class updatePassword extends Detail {
        public String password;
        public String controlPassword;
    }

    public class Barber extends Detail {
        public String barberName;
        public String phoneNo;
        public String adress;
        public String neighborhood;
        public String district;
        public String city;
    }


}
