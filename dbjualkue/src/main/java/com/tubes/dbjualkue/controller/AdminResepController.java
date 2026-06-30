package com.tubes.dbjualkue.controller;

import com.tubes.dbjualkue.entity.Kue;
import com.tubes.dbjualkue.entity.BahanBaku;
import com.tubes.dbjualkue.entity.Resep;
import com.tubes.dbjualkue.repository.KueRepository;
import com.tubes.dbjualkue.repository.BahanBakuRepository;
import com.tubes.dbjualkue.repository.ResepRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/admin/resep")
public class AdminResepController {
    
    private final ResepRepository resepRepository;
    private final KueRepository kueRepository;
    private final BahanBakuRepository bahanBakuRepository;

    public AdminResepController (ResepRepository resepRepository, KueRepository kueRepository, BahanBakuRepository bahanBakuRepository) {
        this.resepRepository = resepRepository;
        this.kueRepository = kueRepository;
        this.bahanBakuRepository = bahanBakuRepository;
    }

    private boolean belumLogin(HttpSession session) {
        return session.getAttribute("adminLogin") == null;
    }

    //List semua resep
   @GetMapping
public String daftarResep(HttpSession session, Model model) {
    if (belumLogin(session)) return "redirect:/admin/login";

    List<Resep> semuaResep = resepRepository.findAll();

    Map<Kue, List<Resep>> resepPerKue = semuaResep.stream()
            .collect(Collectors.groupingBy(Resep::getKue));

    model.addAttribute("resepPerKue", resepPerKue);
    return "admin-resep-list";
}

    //form tambah resep
    @GetMapping("/tambah")
    public String formTambah(HttpSession session, Model model) {
        if (belumLogin(session)) return "redirect:/admin/login";

        model.addAttribute("daftarKue", kueRepository.findAll());
        model.addAttribute("daftarBahan", bahanBakuRepository.findAll());
        return "admin-resep-form";
    }

    // Proses simpan resep baru
    @PostMapping("/tambah")
public String prosesTambah(@RequestParam String idKue,
                            @RequestParam List<String> idBahan,
                            @RequestParam List<Double> jumlahDibutuhkan,
                            HttpSession session) {
    if (belumLogin(session)) return "redirect:/admin/login";

    Kue kue = kueRepository.findById(idKue).orElseThrow();

    for (int i = 0; i < idBahan.size(); i++) {
        // Lewati baris yang sengaja dikosongkan (bahan tidak dipilih)
        if (idBahan.get(i) == null || idBahan.get(i).isBlank()) {
            continue;
        }

        BahanBaku bahan = bahanBakuRepository.findById(idBahan.get(i)).orElseThrow();

        Resep resep = new Resep();
        resep.setIdResep(generateIdResep());
        resep.setKue(kue);
        resep.setBahanBaku(bahan);
        resep.setJumlahDibutuhkan(jumlahDibutuhkan.get(i));
        resepRepository.save(resep);
    }

    return "redirect:/admin/resep";
}

    // Proses hapus resep
    @PostMapping("/hapus/{id}")
    public String hapusResep(@PathVariable String id, HttpSession session) {
        if (belumLogin(session)) return "redirect:/admin/login";

        resepRepository.deleteById(id);
        return "redirect:/admin/resep";
    }

    // Helper generate ID resep otomatis (format: R001, R002, dst)
    private String generateIdResep() {
        List<Resep> semuaResep = resepRepository.findAll();

        int nomorTerbesar = semuaResep.stream()
                .map(r -> r.getIdResep().substring(1))
                .mapToInt(Integer::parseInt)
                .max()
                .orElse(0);

        return String.format("R%03d", nomorTerbesar + 1);
    }
}
