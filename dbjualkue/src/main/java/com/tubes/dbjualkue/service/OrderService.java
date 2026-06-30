package com.tubes.dbjualkue.service;

import com.tubes.dbjualkue.entity.Pesanan;
import java.util.List;


public interface OrderService {
    //proses pesanan baru (termasuk validasi)
    Pesanan prosesPesanan(Pesanan pesanan);

    //ambil daftar FIFO
    List<Pesanan> getJobQueue();

    //ubah status pesanan manual
    Pesanan updateOrderStatus(String idPesanan, String statusBaru);
}
