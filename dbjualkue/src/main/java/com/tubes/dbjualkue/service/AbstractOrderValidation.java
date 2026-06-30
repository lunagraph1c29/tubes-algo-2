package com.tubes.dbjualkue.service;

import com.tubes.dbjualkue.entity.Pesanan;
import com.tubes.dbjualkue.repository.PesananRepository;

import java.time.LocalDate;
import java.util.List;

public abstract class AbstractOrderValidation implements OrderService {
    protected final PesananRepository pesananRepository;

    // aturan global toko
    protected static final int LIMIT_TOTAL_BOKS_HARIAN = 10;
    protected static final int MINIMAL_H_MINUS =2;

    public AbstractOrderValidation(PesananRepository pesananRepository) {
        this.pesananRepository = pesananRepository;
    }

    //validasi aturan H-2
    protected void validasiTanggalPengiriman(LocalDate tanggalPengiriman) {
        LocalDate batasMinimal = LocalDate.now().plusDays(MINIMAL_H_MINUS);
        if (tanggalPengiriman.isBefore(batasMinimal)) {
            throw new IllegalArgumentException(
                "Tanggal pengiriman minimal" + MINIMAL_H_MINUS + " hari dari sekarang."
            );
        }
    }

    //validasi limit books harian (semua jenis kue digabung)
    protected void validasiKuotaHarian(LocalDate tanggalPengiriman, int jumlahBoksBaru) {
        List<Pesanan> pesananDiTanggalItu = pesananRepository.findAll().stream().filter(p -> p.getTanggalPengiriman().equals(tanggalPengiriman)).toList();
    
    int totalBoksSudahAda = pesananDiTanggalItu.stream().mapToInt(Pesanan::getJumlahBoks).sum();

    int sisaKuotaHarian = LIMIT_TOTAL_BOKS_HARIAN - totalBoksSudahAda;

    if (jumlahBoksBaru > sisaKuotaHarian) {
        throw new IllegalArgumentException(
            "Kuota boks harian tersisa " + sisaKuotaHarian + " boks untuk tanggal tersebut. Silakan masukan tanggal yang lain."
        );
        }
    }

    //method ga tulis nanti dipaksa diisinya sama Concrete Class 
    protected abstract void validasiKuotaSpesifikMenu(Pesanan pesanan);
}
