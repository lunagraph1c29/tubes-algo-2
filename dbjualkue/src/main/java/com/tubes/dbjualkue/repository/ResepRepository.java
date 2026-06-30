package com.tubes.dbjualkue.repository;

import com.tubes.dbjualkue.entity.Resep;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResepRepository extends JpaRepository<Resep, String> {

    // Cari semua resep untuk satu jenis kue tertentu
    List<Resep> findByKueIdKue(String idKue);
}