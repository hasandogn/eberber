package com.demo.eberber.domain;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;
import java.io.Serializable;


@Entity
@Table(name = "Service")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceBarber implements Serializable{
    private static final long serialVersionID = 4048798961366546485L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String typeName;
    private int price;
    private int barberId;
    private int time;


    public void setId(Long id) {
        this.id = id;
    }
    public static long getSerialVersionID() {
        return serialVersionID;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return typeName;
    }

    public void setName(String typeName) {
        this.typeName = typeName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getBarberId() {
        return barberId;
    }

    public void setBarberId(int barberId) {
        this.barberId = barberId;
    }

}
//id time typeName price