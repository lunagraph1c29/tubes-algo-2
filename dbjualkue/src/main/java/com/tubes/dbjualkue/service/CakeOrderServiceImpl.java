package com.tubes.dbjualkue.service;

import com.tubes.dbjualkue.entity.Pesanan;
import com.tubes.dbjualkue.entity.BahanBaku;
import com.tubes.dbjualkue.entity.Resep;
import com.tubes.dbjualkue.repository.BahanBakuRepository;
import com.tubes.dbjualkue.repository.ResepRepository;
import com.tubes.dbjualkue.repository.PesananRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CakeOrderServiceImpl extends AbstractOrderValidation {

    private static final int LIMIT_BOKS_PER_JENIS_KUE = 5;

    private final ResepRepository resepRepository;
    private final BahanBakuRepository bahanBakuRepository;

public CakeOrderServiceImpl(PesananRepository pesananRepository,
                             ResepRepository resepRepository,
                             BahanBakuRepository bahanBakuRepository) {
    super(pesananRepository);
    this.resepRepository = resepRepository;
    this.bahanBakuRepository = bahanBakuRepository;

    }

private void validasiDanKurangiStok(Pesanan pesanan) {
        List<Resep> resepKueIni = resepRepository.findByKueIdKue(pesanan.getKue().getIdKue());

        for (Resep r : resepKueIni) {
            double kebutuhan = r.getJumlahDibutuhkan() * pesanan.getJumlahBoks();
            BahanBaku bahan = r.getBahanBaku();

            if (bahan.getStok() < kebutuhan) {
                throw new IllegalArgumentException(
                    "Stok " + bahan.getNamaBahan() + " tidak cukup. Tersedia "
                    + bahan.getStok() + " " + bahan.getSatuan()
                    + ", dibutuhkan " + kebutuhan + " " + bahan.getSatuan() + "."
                );
            }
        }

        for (Resep r : resepKueIni) {
            double kebutuhan = r.getJumlahDibutuhkan() * pesanan.getJumlahBoks();
            BahanBaku bahan = r.getBahanBaku();

            bahan.setStok(bahan.getStok() - kebutuhan);
            bahanBakuRepository.save(bahan);
        }
    }

    //ngisi method abstract dari parent class
    @Override
    protected void validasiKuotaSpesifikMenu(Pesanan pesanan) {
        List<Pesanan> pesananJenisIniDiTanggalItu = pesananRepository.findAll().stream().filter(p -> p.getTanggalPengiriman().equals(pesanan.getTanggalPengiriman())).filter(p -> p.getKue().getIdKue().equals(pesanan.getKue().getIdKue())).toList();

        int totalBoksJenisIni = pesananJenisIniDiTanggalItu.stream().mapToInt(Pesanan::getJumlahBoks).sum();
        int sisaKuota = LIMIT_BOKS_PER_JENIS_KUE - totalBoksJenisIni;

       if (pesanan.getJumlahBoks() > sisaKuota) {
        throw new IllegalArgumentException(
            "Kuota untuk jenis kue ini tersisa " + sisaKuota + " boks untuk dikirim di tanggal tersebut.");
    }

    }

    

// ngisi method interface OrderService
    @Override
    public Pesanan prosesPesanan(Pesanan pesanan) {
        //validasi global (dari abstract class)
        validasiTanggalPengiriman(pesanan.getTanggalPengiriman());
        validasiKuotaHarian(pesanan.getTanggalPengiriman(), pesanan.getJumlahBoks());

         //validasi spesifikmenu
        validasiKuotaSpesifikMenu(pesanan);

        //validasi kurangi stok bahan 
        validasiDanKurangiStok(pesanan);

         //generate id pesanan otomatis
        pesanan.setIdPesanan(generateIdPesanan());

        //set waktu pesan otomatis (dasar urutan FIFO)
        pesanan.setWaktuPesan(LocalDateTime.now());

        //status default
        pesanan.setStatus("Menunggu");

        return pesananRepository.save(pesanan);
    }

    @Override
    public List <Pesanan> getJobQueue() {
        return pesananRepository.findAllByOrderByWaktuPesanAsc();
    }

    @Override
    public Pesanan updateOrderStatus(String idPesanan, String statusBaru) {
        Pesanan pesanan = pesananRepository.findById(idPesanan).orElseThrow(() -> new IllegalArgumentException("Pesanan tidak di temukan."));

        pesanan.setStatus(statusBaru);
        return pesananRepository.save(pesanan);
    }

    // Helper generate ID pesanan otomatis (format: P00001, P00002, dst)
    private String generateIdPesanan() {
        List<Pesanan> semuaPesanan = pesananRepository.findAll();

        int nomorTerbesar = semuaPesanan.stream()
                .map(p -> p.getIdPesanan().substring(1))
                .mapToInt(Integer::parseInt)
                .max()
                .orElse(0);

        return String.format("P%05d", nomorTerbesar + 1);
}
}
