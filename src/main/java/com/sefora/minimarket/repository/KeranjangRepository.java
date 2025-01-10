package com.sefora.minimarket.repository;

import com.sefora.minimarket.model.Keranjang;
import com.sefora.minimarket.model.Pembeli;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KeranjangRepository extends JpaRepository<Keranjang, Long> {
    List<Keranjang> findByPembeli(Pembeli pembeli);

    void deleteByPembeli(Pembeli pembeli);

}
