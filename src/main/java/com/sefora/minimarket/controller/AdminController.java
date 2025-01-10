package com.sefora.minimarket.controller;

import com.sefora.minimarket.model.Keranjang;
import com.sefora.minimarket.model.Pembeli;
import com.sefora.minimarket.model.Barang;
import com.sefora.minimarket.service.KeranjangService;
import com.sefora.minimarket.service.TransaksiService;
import com.sefora.minimarket.service.BarangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private KeranjangService keranjangService;

    @Autowired
    private TransaksiService transaksiService;

    @Autowired
    private BarangService barangService;

    // Admin Dashboard
    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        // Ambil semua barang yang tersedia
        model.addAttribute("barangList", barangService.getAllBarang());
        return "admin/dashboard";
    }

    // Menambah barang ke keranjang
    @PostMapping("/addToKeranjang")
    public String addToKeranjang(@RequestParam Long barangId, @RequestParam Integer jumlah, Model model, @ModelAttribute("pembeli") Pembeli pembeli) {
        Barang barang = barangService.getBarangById(barangId);  // Get the Barang object

        if (barang != null && jumlah > 0) {
            // Call addBarangToKeranjang method with Pembeli, Barang, and jumlah directly
            keranjangService.addBarangToKeranjang(pembeli, barang, jumlah);
        }

        return "redirect:/admin/dashboard";
    }

    // Checkout
    @PostMapping("/checkout")
    public String checkout(@ModelAttribute("pembeli") Pembeli pembeli) {
        transaksiService.createTransaksi(pembeli);
        return "redirect:/admin/dashboard";  // Redirect ke dashboard setelah checkout
    }
}
