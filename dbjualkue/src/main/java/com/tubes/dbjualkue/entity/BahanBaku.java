package com.tubes.dbjualkue.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "bahan_baku")
public class BahanBaku {

@Id
@Column(name = "id_bahan", length = 4)
private String idBahan;

@Column(name = "nama_bahan", length = 50)
private String namaBahan;

@Column(name = "stok")
private Double stok;

@Column(name = "satuan", length = 20)
private String satuan;

public BahanBaku() {
}

public String getIdBahan() {
    return idBahan;
}

public void setIdBahan(String idBahan) {
    this.idBahan = idBahan;
}

public String getNamaBahan() {
    return namaBahan;
}

public void setNamaBahan(String namaBahan) {
    this.namaBahan = namaBahan;
}

public Double getStok() {
    return stok;
}

public void setStok(Double stok) {
    this.stok = stok;
}

public String getSatuan() {
    return satuan;
}

public void setSatuan(String satuan) {
    this.satuan = satuan;
}

}