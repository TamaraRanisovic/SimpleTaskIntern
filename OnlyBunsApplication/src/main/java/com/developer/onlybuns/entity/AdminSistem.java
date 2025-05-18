package com.developer.onlybuns.entity;


import javax.persistence.*;

@Entity
@Table(name="adminSistema")
public class AdminSistem extends Korisnik {

    public AdminSistem() {
        this.glavni = false;
    }

    @Column(name = "glavni")
    private Boolean glavni;

    public Boolean getGlavni() {
        return glavni;
    }

    public void setGlavni(Boolean glavni) {
        this.glavni = glavni;
    }
}
