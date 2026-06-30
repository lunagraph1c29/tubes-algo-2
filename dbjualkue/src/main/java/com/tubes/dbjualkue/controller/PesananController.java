package com.tubes.dbjualkue.controller;

import com.tubes.dbjualkue.entity.Pesanan;
import com.tubes.dbjualkue.repository.KueRepository;
import com.tubes.dbjualkue.repository.PesananRepository;
import com.tubes.dbjualkue.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class PesananController {

    private final OrderService orderService;
    private final KueRepository kueRepository;
    private final PesananRepository pesananRepository;

    @Autowired
    public PesananController(OrderService orderService, KueRepository kueRepository, PesananRepository pesananRepository) {
        this.orderService = orderService;
        this.kueRepository = kueRepository;
        this.pesananRepository = pesananRepository;
    }

    // Menampilkan halaman home/landing page
@GetMapping("/")
public String tampilkanHome(Model model) {
    model.addAttribute("daftarKue", kueRepository.findAll());
    return "home";
    }

    // Menampilkan halaman form pemesanan
    @GetMapping("/pesan")
    public String tampilkanFormPesan(Model model) {
        model.addAttribute("pesanan", new Pesanan());
        model.addAttribute("daftarKue", kueRepository.findAll());
        return "form-pesan";
    }

    // Memproses submit form pemesanan
    @PostMapping("/pesan")
    public String prosesPesan(@ModelAttribute Pesanan pesanan, Model model) {
        try {
            Pesanan hasil = orderService.prosesPesanan(pesanan);
            model.addAttribute("pesanan", hasil);
            return "pesan-sukses";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("daftarKue", kueRepository.findAll());
            return "form-pesan";
        }
    }

    // Menampilkan halaman cek status (form input No HP)
    @GetMapping("/cek-status")
    public String tampilkanFormCekStatus() {
        return "cek-status";
    }

    // Memproses cek status berdasarkan No HP
    @GetMapping("/cek-status/hasil")
    public String prosesCekStatus(@RequestParam String noHp, Model model) {
        List<Pesanan> daftarPesanan = pesananRepository.findByNoHp(noHp);
        model.addAttribute("daftarPesanan", daftarPesanan);
        model.addAttribute("noHp", noHp);
        return "hasil-status";
    }
}