package com.demo.eberber.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "HoursStatus")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HoursStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @PrimaryKeyJoinColumn
    private long id;
    private long staffId;
    private String day;
    private String hour;
    private String emptyIsHours;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getStaffId() {
        return staffId;
    }

    public void setStaffId(long staffId) {
        this.staffId = staffId;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getEmptyIsHours() {
        return emptyIsHours;
    }

    public void setEmptyIsHours(String emptyIsHours) {
        this.emptyIsHours = emptyIsHours;
    }
}
