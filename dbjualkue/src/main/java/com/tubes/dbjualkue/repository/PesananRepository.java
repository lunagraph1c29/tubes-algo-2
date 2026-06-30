package com.tubes.dbjualkue.repository;

import com.tubes.dbjualkue.entity.Pesanan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;



public interface PesananRepository extends JpaRepository<Pesanan, String> {
    //fitur lacak pake no hp
    List<Pesanan>findByNoHp(String noHp);

    //fitur antrian FIFO: urut berdasarkan waktu pesan dari yang terlama
    List<Pesanan>findAllByOrderByWaktuPesanAsc();
}
