package com.demo.eberber.domain;

public class Address {
    private String adresDetay;
    private long id;
    private long barberId;
    private String addressCity;

    public String getAddressCity() {
        return addressCity;
    }

    public void setAddressCity(String addressCity) {
        this.addressCity = addressCity;
    }

    public String getAdresDetay() {
        return adresDetay;
    }

    public void setAdresDetay(String adresDetay) {
        this.adresDetay = adresDetay;
    }

    public long getBarberId() {
        return barberId;
    }

    public void setBarberId(long barberId) {
        this.barberId = barberId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }



}
