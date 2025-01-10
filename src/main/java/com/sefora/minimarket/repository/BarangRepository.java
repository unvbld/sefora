package com.sefora.minimarket.repository;

import com.sefora.minimarket.model.Barang;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BarangRepository extends JpaRepository<Barang, Long> {
    // No need to write any additional methods if you just want to fetch by ID
}
