package com.demo.eberber.domain;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Entity
@Table(name = "Staff")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Staff implements  Serializable{
    private static final long serialVersionUID = 4048798961366546485L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String staffName;
    private Long barberId;
    private Long appointmentsId;
    private Long servicesId;
    private boolean staffSex;
    private Long revenue;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public Long getBarberId() {
        return barberId;
    }

    public void setBarberId(Long barberId) {
        this.barberId = barberId;
    }

    public Long getAppointmentsId() {
        return appointmentsId;
    }

    public void setAppointmentsId(Long appointmentsId) {
        this.appointmentsId = appointmentsId;
    }

    public Long getServicesId() {
        return servicesId;
    }

    public void setServicesId(Long servicesId) {
        this.servicesId = servicesId;
    }

    public boolean isStaffSex() {
        return staffSex;
    }

    public void setStaffSex(boolean staffSex) {
        this.staffSex = staffSex;
    }

    public Long getRevenue() {
        return revenue;
    }

    public void setRevenue(Long revenue) {
        this.revenue = revenue;
    }
}
//id,staffName,barberId,appointments,servicesId,staffSex,revenue