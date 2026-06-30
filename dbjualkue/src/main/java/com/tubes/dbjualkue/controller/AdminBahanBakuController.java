package com.tubes.dbjualkue.controller;

import com.tubes.dbjualkue.entity.BahanBaku;
import com.tubes.dbjualkue.repository.BahanBakuRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/bahan")
public class AdminBahanBakuController {
    
    private final BahanBakuRepository bahanBakuRepository;
    public AdminBahanBakuController(BahanBakuRepository bahanBakuRepository) {
        this.bahanBakuRepository = bahanBakuRepository;
    }

    private boolean belumLogin(HttpSession session) {
        return session.getAttribute("adminLogin") == null;
    }

      // Daftar bahan baku
    @GetMapping
    public String daftarBahan(HttpSession session, Model model) {
        if (belumLogin(session)) return "redirect:/admin/login";

        model.addAttribute("daftarBahan", bahanBakuRepository.findAll());
        return "admin-bahan-list";
    }

    // Form tambah bahan baru
    @GetMapping("/tambah")
    public String formTambah(HttpSession session, Model model) {
        if (belumLogin(session)) return "redirect:/admin/login";

        model.addAttribute("bahan", new BahanBaku());
        return "admin-bahan-form";
    }

    // Proses simpan bahan baru
    @PostMapping("/tambah")
    public String prosesTambah(@RequestParam String namaBahan,
                                @RequestParam Double stok,
                                @RequestParam String satuan,
                                HttpSession session) {
        if (belumLogin(session)) return "redirect:/admin/login";

        BahanBaku bahan = new BahanBaku();
        bahan.setIdBahan(generateIdBahan());
        bahan.setNamaBahan(namaBahan);
        bahan.setStok(stok);
        bahan.setSatuan(satuan);
        bahanBakuRepository.save(bahan);

        return "redirect:/admin/bahan";
    }

    //form edit bahan
    @GetMapping("/edit/{id}")
    public String formEdit(@PathVariable String id, HttpSession session, Model model) {
        if (belumLogin(session)) return "redirect:/admin/login";

        BahanBaku bahan = bahanBakuRepository.findById(id).orElseThrow();
        model.addAttribute("bahan", bahan);
        return "admin-bahan-edit";
    }

    //proses edit bahan
    @PostMapping("/edit/{id}")
    public String prosesEdit(@PathVariable String id,
                                @RequestParam String namaBahan,
                                @RequestParam Double stok,
                                @RequestParam String satuan,
                                HttpSession session) {
        if (belumLogin(session)) return "redirect:/admin/login";
        
        BahanBaku bahan = bahanBakuRepository.findById(id).orElseThrow();
        bahan.setNamaBahan(namaBahan);
        bahan.setStok(stok);
        bahan.setSatuan(satuan);
        bahanBakuRepository.save(bahan);

        return "redirect:/admin/bahan";

    }
    
    //proses hapus bahan
    @PostMapping("/hapus/{id}")
    public String hapusBahan(@PathVariable String id, HttpSession session) {
        if (belumLogin(session)) return "redirect:/admin/login";

        bahanBakuRepository.deleteById(id);
        return "redirect:/admin/bahan";
    }
    
    //helper generate id
    private String generateIdBahan() {
        List<BahanBaku> semuaBahan = bahanBakuRepository.findAll();

        int nomorTerbesar = semuaBahan.stream().map(b -> b.getIdBahan().substring(1)).mapToInt(Integer::parseInt).max().orElse(0);
    return String.format("B%03d", nomorTerbesar + 1);
    }
}
