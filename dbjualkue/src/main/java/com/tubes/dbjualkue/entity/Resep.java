package com.tubes.dbjualkue.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "resep")
public class Resep {

@Id
@Column(name = "id_resep", length = 4)
private String idResep;

@ManyToOne
@JoinColumn(name = "id_kue", referencedColumnName = "id_kue")
private Kue kue;

@ManyToOne
@JoinColumn(name = "id_bahan", referencedColumnName = "id_bahan")
private BahanBaku bahanBaku;

@Column(name = "jumlah_dibutuhkan")
private Double jumlahDibutuhkan;

public Resep() {
}

public String getIdResep() {
    return idResep;
}

public void setIdResep(String idResep) {
    this.idResep = idResep;
}

public Kue getKue() {
    return kue;
}

public void setKue(Kue kue) {
    this.kue = kue;
}

public BahanBaku getBahanBaku() {
    return bahanBaku;
}

public void setBahanBaku(BahanBaku bahanBaku) {
    this.bahanBaku = bahanBaku;
}

public Double getJumlahDibutuhkan() {
    return jumlahDibutuhkan;
}

public void setJumlahDibutuhkan(Double jumlahDibutuhkan) {
    this.jumlahDibutuhkan = jumlahDibutuhkan;
}

}