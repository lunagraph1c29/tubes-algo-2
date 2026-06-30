package com.tubes.dbjualkue.controller;

import com.tubes.dbjualkue.entity.Admin;
import com.tubes.dbjualkue.entity.Pesanan;
import com.tubes.dbjualkue.repository.AdminRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.tubes.dbjualkue.repository.PesananRepository;

import java.util.Optional;

@Controller
public class AdminController {

private final AdminRepository adminRepository;
private final PesananRepository pesananRepository;

public AdminController(AdminRepository adminRepository, PesananRepository pesananRepository) {
    this.adminRepository = adminRepository;
    this.pesananRepository = pesananRepository;
}

    // Menampilkan form login
    @GetMapping("/admin/login")
    public String tampilkanFormLogin() {
        return "admin-login";
    }

    // Memproses login
    @PostMapping("/admin/login")
    public String prosesLogin(@RequestParam String username,
                               @RequestParam String password,
                               HttpSession session,
                               Model model) {

        Optional<Admin> adminDitemukan = adminRepository.findByUsernameAndPassword(username, password);

        if (adminDitemukan.isPresent()) {
            // Login berhasil -> simpan tanda di session
            session.setAttribute("adminLogin", adminDitemukan.get().getUsername());
            return "redirect:/admin/dashboard";
        } else {
            // Login gagal -> tampilkan pesan error
            model.addAttribute("errorMessage", "Username atau password salah.");
            return "admin-login";
        }
    }

    // Logout
    @GetMapping("/admin/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login";
    }

    @GetMapping("/admin/dashboard")
public String tampilkanDashboard(HttpSession session, Model model) {
    // Cek "gelang" -> kalau tidak ada, tendang ke login
    if (session.getAttribute("adminLogin") == null) {
        return "redirect:/admin/login";
    }

    model.addAttribute("daftarAntrean", pesananRepository.findAllByOrderByWaktuPesanAsc());
    model.addAttribute("namaAdmin", session.getAttribute("adminLogin"));
    return "admin-dashboard";
}

// Update status pesanan
@PostMapping("/admin/update-status")
public String updateStatus(@RequestParam String idPesanan,
                            @RequestParam String statusBaru,
                            HttpSession session) {

    if (session.getAttribute("adminLogin") == null) {
        return "redirect:/admin/login";
    }

    Pesanan pesanan = pesananRepository.findById(idPesanan).orElseThrow();
    pesanan.setStatus(statusBaru);
    pesananRepository.save(pesanan);

    return "redirect:/admin/dashboard";
}
}