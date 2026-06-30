package com.tubes.dbjualkue.entity;


import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "pesanan")
public class Pesanan {

    @Id
    @Column(name = "id_pesanan", length = 6)
    private String idPesanan;

    @Column(name = "nama_pembeli", length = 50)
    private String namaPembeli;

    @Column(name = "no_hp", length = 20)
    private String noHp;

    @ManyToOne
    @JoinColumn(name = "id_kue", referencedColumnName = "id_kue")
    private Kue kue;

    @Column(name = "jumlah_boks")
    private Integer jumlahBoks;

    @Column(name = "tanggal_pengiriman")
    private LocalDate tanggalPengiriman;

    @Column(name = "waktu_pesan")
    private LocalDateTime waktuPesan;

    @Column(name = "status", length = 20)
    private String status;

    public Pesanan() {    
    }

    //getter and setter
    public String getIdPesanan() {
        return idPesanan;
    }

    public void setIdPesanan(String idPesanan) {
        this.idPesanan = idPesanan; 
    }

    public String getNamaPembeli() {
        return namaPembeli;
    }

    public void setNamaPembeli(String namaPembeli) {
        this.namaPembeli = namaPembeli;
    }
    
    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

    public Kue getKue() {
        return kue;
    }

    public void setKue(Kue kue) {
        this.kue = kue;
    }

    public Integer getJumlahBoks() {
        return jumlahBoks;
    }

    public void setJumlahBoks(Integer jumlahBoks) {
        this.jumlahBoks = jumlahBoks;
    }

    public LocalDate getTanggalPengiriman() {
        return tanggalPengiriman;
    }

    public void setTanggalPengiriman(LocalDate tanggalPengiriman) {
        this.tanggalPengiriman = tanggalPengiriman;
    }

    public LocalDateTime getWaktuPesan() {
        return waktuPesan;
    }

    public void setWaktuPesan(LocalDateTime waktuPesan) {
        this.waktuPesan = waktuPesan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}