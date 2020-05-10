package com.demo.eberber.domain;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "Appointment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Appointment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @PrimaryKeyJoinColumn
    private long id;
    @Column(name="barberId")
    private long barberId;
    @Column(name="customerId")
    private long customerId;
    @Column(name="appointmentDate")
    private Date appointmentDate;
    @Column(name="serviceId")
    private int serviceId;
    @Column(name="staffId")
    private long staffId;


    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getBarberId() {
        return barberId;
    }

    public void setBarberId(int barberId) {
        this.barberId = barberId;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public Date setAppointmentDate(Date appointmentDate) {
        return this.appointmentDate = appointmentDate;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
         this.serviceId = serviceId;
    }

    public long getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }
}