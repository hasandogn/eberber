package com.demo.eberber.domain;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.io.Serializable;


@Entity
@Table(name = "Barber")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Barber implements Serializable {

    private static final long serialVersionUID = 4048798961366546485L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;
    @Column(name="barberName")
    private String barberName;
    @Column(name="phoneNo")
    private String phoneNo;
    @Column(name="eMail")
    private String eMail;
    @Column(name="adress")
    private String adress;
    @Column(name="neighborhood")
    private String neighborhood;
    @Column(name="district")
    private String district;
    @Column(name="city")
    private String city;
    @Column(name="password")
    private String password;
    @Column(name="Comment")
    private String Comment;

    public void setId(long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getBarberName() {
        return barberName;
    }

    public void setBarberName(String barberName) {
        this.barberName = barberName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phone) {
        this.phoneNo = phone;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }


}