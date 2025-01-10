package com.sefora.minimarket.controller;

import com.sefora.minimarket.model.*;
import com.sefora.minimarket.service.KeranjangService;
import com.sefora.minimarket.service.BarangService;
import com.sefora.minimarket.service.TransaksiService;
import com.sefora.minimarket.repository.PembeliRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private KeranjangService keranjangService;

    @Autowired
    private BarangService barangService;

    @Autowired
    private TransaksiService transaksiService;

    @Autowired
    private HttpSession session;

    @Autowired
    private PembeliRepository pembeliRepository;

    // Get the logged-in Pembeli from the session
    private Pembeli getLoggedInUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return pembeliRepository.findByUsername(username).orElse(null);
    }

    @GetMapping("/user/dashboard")
    public String userDashboard(Model model) {
        Pembeli pembeli = getLoggedInUser();
        if (pembeli == null) {
            return "redirect:/login?error=Please log in to continue.";
        }

        model.addAttribute("barangList", barangService.getAllBarang());
        return "user/dashboard";
    }

    @GetMapping("/user/cart")
    public String showCart(Model model) {
        Pembeli pembeli = getLoggedInUser();
        if (pembeli == null) {
            return "redirect:/login?error=Please log in to continue.";
        }

        List<Keranjang> keranjangList = keranjangService.getKeranjangByPembeli(pembeli);
        BigDecimal totalHarga = keranjangList.stream()
                .map(k -> k.getBarang().getHarga().multiply(new BigDecimal(k.getJumlah())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("keranjangList", keranjangList);
        model.addAttribute("totalHarga", totalHarga);
        return "user/cart";
    }

    @PostMapping("/user/cart/add")
    public String addToCart(@RequestParam Long barangId, @RequestParam int jumlah, Model model) {
        Pembeli pembeli = getLoggedInUser();
        if (pembeli == null) {
            return "redirect:/login?error=Please log in to continue.";
        }

        Barang barang = barangService.getBarangById(barangId);
        if (barang != null && jumlah > 0) {
            keranjangService.addBarangToKeranjang(pembeli, barang, jumlah);
        }
        return "redirect:/user/cart";
    }

    @PostMapping("/user/cart/remove")
    public String removeFromCart(@RequestParam Long keranjangId) {
        Pembeli pembeli = getLoggedInUser();
        if (pembeli == null) {
            return "redirect:/login?error=Please log in to continue.";
        }

        keranjangService.deleteBarangFromKeranjang(keranjangId);
        return "redirect:/user/cart";
    }

    @PostMapping("/user/checkout")
    public String checkout(@RequestParam(required = false) String metodePembayaran, Model model) {
        Pembeli pembeli = getLoggedInUser();
        if (pembeli == null) {
            return "redirect:/login?error=Please log in to continue.";
        }

        List<Keranjang> keranjangList = keranjangService.getKeranjangByPembeli(pembeli);
        if (keranjangList.isEmpty()) {
            model.addAttribute("errorMessage", "Keranjang Anda kosong.");
            return "redirect:/user/cart";
        }

        if (metodePembayaran == null || metodePembayaran.isEmpty()) {
            model.addAttribute("errorMessage", "Please select a payment method.");
            return "redirect:/user/cart";
        }

        BigDecimal totalHarga = keranjangList.stream()
                .map(k -> k.getBarang().getHarga().multiply(new BigDecimal(k.getJumlah())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Transaksi transaksi = new Transaksi();
        transaksi.setPembeli(pembeli);
        transaksi.setTotalHarga(totalHarga);
        transaksi.setMetodePembayaran(MetodePembayaran.valueOf(metodePembayaran));
        transaksi.setTanggalTransaksi(LocalDateTime.now());

        transaksiService.saveTransaksi(transaksi);
        keranjangService.clearCart(pembeli);

        model.addAttribute("success", "Transaction completed successfully!");
        return "redirect:/user/transaction-success";  // Redirect to transaction success page
    }

    @PostMapping("/user/logout")
    public String logout() {
        SecurityContextHolder.clearContext();  // Clear Spring Security context
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/user/transaction-success")
    public String transactionSuccess() {
        return "user/transaction-success";
    }
}
