package com.tubes.dbjualkue.controller;

import com.tubes.dbjualkue.entity.Kue;
import com.tubes.dbjualkue.repository.KueRepository;
import com.tubes.dbjualkue.repository.PesananRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/kue")
public class AdminKueController {

    private final KueRepository kueRepository;
    private final PesananRepository pesananRepository;

    public AdminKueController(KueRepository kueRepository, PesananRepository pesananRepository) {
        this.kueRepository = kueRepository;
        this.pesananRepository = pesananRepository;
    }

    //cek login
    private boolean belumLogin(HttpSession session) {
        return session.getAttribute("adminLogin") == null;
    }



    //tampil daftar kue
    @GetMapping
    public String daftarKue(HttpSession session, Model model) {
        if (belumLogin(session)) return "redirect:/admin/login";

        model.addAttribute("daftarKue", kueRepository.findAll());
        return "admin-kue-list";
    }

    //form tambah kue baru
    @GetMapping("/tambah")
    public String formTambah(HttpSession session, Model model) {
        if (belumLogin(session)) return "redirect:/admin/login";

        model.addAttribute("kue", new Kue());
        return "admin-kue-form";
    }

    //proses simpan kue baru
    @PostMapping("/tambah")
    public String prosesTambah(@RequestParam String namaKue,
                                @RequestParam java.math.BigDecimal harga,
                                HttpSession session) {
    if (belumLogin(session)) return "redirect:/admin/login";
        
    Kue kue = new Kue ();
    kue.setIdKue(generateIdKue());
    kue.setNamaKue(namaKue);
    kue.setHarga (harga);
    kueRepository.save(kue);

    return "redirect:/admin/kue";
}

//form edit kue
@GetMapping("/edit/{id}")
public String formEdit(@PathVariable String id, HttpSession session, Model model) {
    if (belumLogin(session)) return "redirect:/admin/login";

    Kue kue = kueRepository.findById(id).orElseThrow();
    model.addAttribute( "kue", kue);
    return "admin-kue-edit";
}

//proses update kue
@PostMapping("/edit/{id}")
public String prosesEdit(@PathVariable String id,
                            @RequestParam String namaKue,
                            @RequestParam java.math.BigDecimal harga,
                            HttpSession session) {
if (belumLogin(session)) return "redirect:admin/login";

Kue kue = kueRepository.findById(id).orElseThrow();
kue.setNamaKue(namaKue);
kue.setHarga(harga);
kueRepository.save(kue);

return "redirect:/admin/kue";

}

//proses hapus kue
@PostMapping("/hapus/{id}")
public String hapusKue(@PathVariable String id, HttpSession session, Model model){
    if (belumLogin(session)) return "redirect:/admin/login";

    boolean masihDipakai = !pesananRepository.findAll().stream()
                            .filter(p -> p.getKue().getIdKue().equals(id))
                            .toList().isEmpty();
    
    if (masihDipakai) {
        model.addAttribute("errorMessage", "Kue tidak bisa dihapus karena masih ada di riwayat pesanan.");
        model.addAttribute("daftarKue", kueRepository.findAll());
        return "admin-kue-list";
    }

    kueRepository.deleteById(id);
    return "redirect:/admin/kue";
    }

    private String generateIdKue() {
    List<Kue> semuaKue = kueRepository.findAll();
    
    int nomorTerbesar = semuaKue.stream()
            .map(k -> k.getIdKue().substring(1))  // ambil bagian angkanya saja, misal "001" dari "K001"
            .mapToInt(Integer::parseInt)
            .max()
            .orElse(0);  // kalau belum ada kue sama sekali, mulai dari 0

    return String.format("K%03d", nomorTerbesar + 1);
}

}
