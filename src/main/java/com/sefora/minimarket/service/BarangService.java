package com.sefora.minimarket.service;

import com.sefora.minimarket.model.Barang;
import com.sefora.minimarket.repository.BarangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BarangService {

    @Autowired
    private BarangRepository barangRepository;

    // Get Barang by ID
    public Barang getBarangById(Long barangId) {
        Optional<Barang> barang = barangRepository.findById(barangId);
        return barang.orElseThrow(() -> new RuntimeException("Barang not found with id " + barangId));
    }

    // Get all Barang
    public List<Barang> getAllBarang() {
        return barangRepository.findAll();
    }

    // Add Barang
    public Barang addBarang(Barang barang) {
        return barangRepository.save(barang);
    }

    // Update Barang
    public Barang updateBarang(Barang barang) {
        Optional<Barang> existingBarangOptional = barangRepository.findById(barang.getId());
        if (existingBarangOptional.isPresent()) {
            Barang existingBarang = existingBarangOptional.get();
            existingBarang.setNamaBarang(barang.getNamaBarang());
            existingBarang.setKategori(barang.getKategori());
            existingBarang.setStok(barang.getStok());
            existingBarang.setHarga(barang.getHarga());
            return barangRepository.save(existingBarang);
        } else {
            throw new RuntimeException("Barang not found with id " + barang.getId());
        }
    }

    // Delete Barang
    public void deleteBarang(Long id) {
        Optional<Barang> existingBarang = barangRepository.findById(id);
        if (existingBarang.isPresent()) {
            barangRepository.deleteById(id);
        } else {
            throw new RuntimeException("Barang not found with id " + id);
        }
    }
}
