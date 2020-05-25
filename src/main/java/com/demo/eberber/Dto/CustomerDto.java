package com.demo.eberber.Dto;

public class CustomerDto {
    public class Detail {
        public int id;
    }

    public class Login {
        public String mail;
        public String password;
    }

    public class Address {
        public String address;
        public String district;
        public String neighborhood;
        public String city;
    }


    public class updatePassword extends Detail {
        public String password;
        public String controlPassword;
    }

    public class changePassword {
        public String eMail;
        public String password;
        public String controlPassword;
    }
}
