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
    private Long id;
    @NotBlank
    private String typeName;

    @NotBlank
    private Long price;

    public static long getSerialVersionID() {
        return serialVersionID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    @NotBlank
    private Long time;
}
//id time typeName price