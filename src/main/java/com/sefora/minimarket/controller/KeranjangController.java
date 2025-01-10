package com.sefora.minimarket.controller;

import com.sefora.minimarket.model.Barang;
import com.sefora.minimarket.model.Keranjang;
import com.sefora.minimarket.model.Pembeli;
import com.sefora.minimarket.service.KeranjangService;
import com.sefora.minimarket.service.BarangService;
import com.sefora.minimarket.repository.PembeliRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class KeranjangController {

    @Autowired
    private KeranjangService keranjangService;

    @Autowired
    private BarangService barangService;

    @Autowired
    private PembeliRepository pembeliRepository;

    @Autowired
    private HttpSession session;

    // Add to cart
    @PostMapping("/user/addToKeranjang")
    public String addToKeranjang(@RequestParam Long barangId, @RequestParam int jumlah, Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Pembeli pembeli = pembeliRepository.findByUsername(username).orElse(null);

        if (pembeli == null) {
            model.addAttribute("error", "You must be logged in to add items to the cart.");
            return "redirect:/login";
        }

        Barang barang = barangService.getBarangById(barangId);
        if (barang == null) {
            model.addAttribute("error", "The product you are trying to add does not exist.");
            return "redirect:/user/dashboard";
        }

        if (barang.getStok() < jumlah) {
            model.addAttribute("error", "Insufficient stock for the requested quantity.");
            return "redirect:/user/dashboard";
        }

        keranjangService.addBarangToKeranjang(pembeli, barang, jumlah);

        model.addAttribute("success", "Item added to cart successfully.");
        return "redirect:/user/keranjang";  // Redirect to view cart
    }

    // View cart
    @GetMapping("/user/keranjang")
    public String viewKeranjang(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Pembeli pembeli = pembeliRepository.findByUsername(username).orElse(null);

        if (pembeli == null) {
            return "redirect:/login";
        }

        List<Keranjang> keranjangList = keranjangService.getKeranjangByPembeli(pembeli);
        model.addAttribute("keranjangList", keranjangList);
        return "user/cart";
    }

    // Increase quantity in cart
    @PostMapping("/user/increaseQuantity")
    public String increaseQuantity(@RequestParam Long keranjangId, @RequestParam int jumlah, Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Pembeli pembeli = pembeliRepository.findByUsername(username).orElse(null);

        if (pembeli == null) {
            model.addAttribute("error", "You must be logged in to modify your cart.");
            return "redirect:/login";
        }

        Keranjang keranjang = keranjangService.getKeranjangById(keranjangId);
        if (keranjang == null || !keranjang.getPembeli().equals(pembeli)) {
            model.addAttribute("error", "The item is not in your cart.");
            return "redirect:/user/keranjang";
        }

        Barang barang = keranjang.getBarang();
        if (barang.getStok() < keranjang.getJumlah() + jumlah) {
            model.addAttribute("error", "Insufficient stock for the requested quantity.");
            return "redirect:/user/keranjang";
        }

        keranjang.setJumlah(keranjang.getJumlah() + jumlah);
        keranjangService.saveKeranjang(keranjang);

        barang.setStok(barang.getStok() - jumlah);
        barangService.updateBarang(barang);

        model.addAttribute("success", "Quantity updated successfully.");
        return "redirect:/user/keranjang";
    }

    // Decrease quantity in cart
    @PostMapping("/user/decreaseQuantity")
    public String decreaseQuantity(@RequestParam Long keranjangId, @RequestParam int jumlah, Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Pembeli pembeli = pembeliRepository.findByUsername(username).orElse(null);

        if (pembeli == null) {
            model.addAttribute("error", "You must be logged in to modify your cart.");
            return "redirect:/login";
        }

        Keranjang keranjang = keranjangService.getKeranjangById(keranjangId);
        if (keranjang == null || !keranjang.getPembeli().equals(pembeli)) {
            model.addAttribute("error", "The item is not in your cart.");
            return "redirect:/user/keranjang";
        }

        if (keranjang.getJumlah() <= jumlah) {
            model.addAttribute("error", "You cannot reduce the quantity below the current amount.");
            return "redirect:/user/keranjang";
        }

        keranjang.setJumlah(keranjang.getJumlah() - jumlah);
        keranjangService.saveKeranjang(keranjang);

        Barang barang = keranjang.getBarang();
        barang.setStok(barang.getStok() + jumlah);
        barangService.updateBarang(barang);

        model.addAttribute("success", "Quantity updated successfully.");
        return "redirect:/user/keranjang";
    }

    // Remove item from cart
    @PostMapping("/user/removeFromCart")
    public String removeFromCart(@RequestParam Long keranjangId, Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Pembeli pembeli = pembeliRepository.findByUsername(username).orElse(null);

        if (pembeli == null) {
            model.addAttribute("error", "You must be logged in to modify your cart.");
            return "redirect:/login";
        }

        Keranjang keranjang = keranjangService.getKeranjangById(keranjangId);
        if (keranjang == null || !keranjang.getPembeli().equals(pembeli)) {
            model.addAttribute("error", "The item is not in your cart.");
            return "redirect:/user/keranjang";
        }

        keranjangService.deleteBarangFromKeranjang(keranjangId);

        model.addAttribute("success", "Item removed from cart successfully.");
        return "redirect:/user/keranjang";
    }
}
