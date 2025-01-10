package com.sefora.minimarket.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
public class Barang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String namaBarang;

    @Enumerated(EnumType.STRING)
    private KategoriBarang kategori;

    private Integer stok;

    @Column(precision = 10, scale = 2)  // Precision and scale for price
    private BigDecimal harga;  // Change harga from Double to BigDecimal

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNamaBarang() {
        return namaBarang;
    }

    public void setNamaBarang(String namaBarang) {
        this.namaBarang = namaBarang;
    }

    public KategoriBarang getKategori() {
        return kategori;
    }

    public void setKategori(KategoriBarang kategori) {
        this.kategori = kategori;
    }

    public Integer getStok() {
        return stok;
    }

    public void setStok(Integer stok) {
        this.stok = stok;
    }

    public BigDecimal getHarga() {
        return harga;
    }

    public void setHarga(BigDecimal harga) {
        this.harga = harga;
    }
}
