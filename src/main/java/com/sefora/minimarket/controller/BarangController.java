package com.sefora.minimarket.controller;

import com.sefora.minimarket.model.Barang;
import com.sefora.minimarket.service.BarangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/barang")
public class BarangController {

    @Autowired
    private BarangService barangService;

    // View All Barang
    @GetMapping
    public String getAllBarang(Model model) {
        List<Barang> barangList = barangService.getAllBarang();
        model.addAttribute("barangList", barangList);
        return "barang/list";  // Thymeleaf view path
    }

    // Add Barang Form
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("barang", new Barang());
        return "barang/add";  // Thymeleaf view path for adding
    }

    // Add Barang Handler
    @PostMapping("/add")
    public String addBarang(@ModelAttribute Barang barang, Model model) {
        if (barang.getNamaBarang() == null || barang.getNamaBarang().isEmpty()) {
            model.addAttribute("errorMessage", "Nama barang tidak boleh kosong.");
            return "barang/add"; // Kembali ke halaman add jika nama barang kosong
        }

        // Simpan barang jika valid
        barangService.addBarang(barang);
        return "redirect:/barang";  // Redirect ke list setelah menambahkan barang
    }

    // Edit Barang Form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Barang> barangOptional = Optional.ofNullable(barangService.getBarangById(id));

        if (barangOptional.isPresent()) {
            Barang barang = barangOptional.get();
            model.addAttribute("barang", barang);
            return "barang/edit";  // Thymeleaf view path for editing
        } else {
            // Barang tidak ditemukan, redirect ke list
            model.addAttribute("errorMessage", "Barang tidak ditemukan.");
            return "redirect:/barang";  // Atau tampilkan error custom jika diperlukan
        }
    }

    // Update Barang Handler
    @PostMapping("/edit/{id}")
    public String updateBarang(@PathVariable Long id, @ModelAttribute Barang barang, Model model) {
        if (barang.getNamaBarang() == null || barang.getNamaBarang().isEmpty()) {
            model.addAttribute("errorMessage", "Nama barang tidak boleh kosong.");
            return "barang/edit"; // Kembali ke halaman edit jika nama barang kosong
        }

        barang.setId(id);  // Pastikan ID sudah diatur untuk update
        barangService.updateBarang(barang);
        return "redirect:/barang";  // Redirect ke list setelah update
    }

    // Delete Barang
    @GetMapping("/delete/{id}")
    public String deleteBarang(@PathVariable Long id) {
        barangService.deleteBarang(id);
        return "redirect:/barang";  // Redirect ke list setelah menghapus barang
    }
}
