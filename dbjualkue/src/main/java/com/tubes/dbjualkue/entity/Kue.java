package com.tubes.dbjualkue.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "kue")
public class Kue {


    @Id
    @Column(name = "id_kue", length = 4)
    private String idKue;

    @Column(name = "nama_kue", length = 50)
    private String namaKue;

    @Column(name = "harga", precision = 10, scale = 2)
    private java.math.BigDecimal harga;

    // konstruktor buat jpa
    public Kue() {
    }

    //getter and setter
    public String getIdKue() {
        return idKue;
    }

    public void setIdKue (String idKue) {
        this.idKue = idKue;
    }

    public String getNamaKue() {
        return namaKue;
    }

    public void setNamaKue(String namaKue) {
        this.namaKue = namaKue;
    }

    public java.math.BigDecimal getHarga() {
        return harga;
    }

    public void setHarga(java.math.BigDecimal harga) {
        this.harga = harga;
    }
}