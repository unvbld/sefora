package com.sefora.minimarket.repository;

import com.sefora.minimarket.model.Transaksi;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransaksiRepository extends JpaRepository<Transaksi, Long> {
}
