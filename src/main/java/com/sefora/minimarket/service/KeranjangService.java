package com.sefora.minimarket.service;

import com.sefora.minimarket.model.Keranjang;
import com.sefora.minimarket.model.Pembeli;
import com.sefora.minimarket.model.Barang;
import com.sefora.minimarket.repository.KeranjangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
public class KeranjangService {

    @Autowired
    private KeranjangRepository keranjangRepository;

    @Autowired
    private BarangService barangService;

    // Save Keranjang to DB
    public void saveKeranjang(Keranjang keranjang) {
        keranjangRepository.save(keranjang);
    }

    // Add Barang to Keranjang
    @Transactional
    public void addBarangToKeranjang(Pembeli pembeli, Barang barang, int jumlah) {
        Keranjang keranjang = new Keranjang();
        keranjang.setPembeli(pembeli);
        keranjang.setBarang(barang);
        keranjang.setJumlah(jumlah);

        // Calculate total price
        BigDecimal totalHarga = barang.getHarga().multiply(new BigDecimal(jumlah));
        keranjang.setTotalHarga(totalHarga);

        keranjangRepository.save(keranjang);

        barang.setStok(barang.getStok() - jumlah);
        barangService.updateBarang(barang);
    }

    // Get Keranjang by Pembeli
    public List<Keranjang> getKeranjangByPembeli(Pembeli pembeli) {
        return keranjangRepository.findByPembeli(pembeli);
    }

    // Get Keranjang by ID
    public Keranjang getKeranjangById(Long keranjangId) {
        return keranjangRepository.findById(keranjangId).orElse(null);
    }

    // Delete Barang from Keranjang
    @Transactional
    public void deleteBarangFromKeranjang(Long keranjangId) {
        Keranjang keranjang = keranjangRepository.findById(keranjangId).orElse(null);
        if (keranjang != null) {
            keranjangRepository.delete(keranjang);
        }
    }

    // Clear Keranjang (empty cart)
    @Transactional
    public void clearCart(Pembeli pembeli) {
        List<Keranjang> keranjangList = keranjangRepository.findByPembeli(pembeli);
        if (keranjangList != null && !keranjangList.isEmpty()) {
            keranjangRepository.deleteAll(keranjangList);
        }
    }
}
