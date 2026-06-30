package com.tubes.dbjualkue.repository;

import com.tubes.dbjualkue.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, String> {

    // Untuk cek login cari admin berdasarkan username DAN password
    Optional<Admin> findByUsernameAndPassword(String username, String password);
}