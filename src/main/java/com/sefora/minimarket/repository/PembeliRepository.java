package com.sefora.minimarket.repository;

import com.sefora.minimarket.model.Pembeli;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PembeliRepository extends JpaRepository<Pembeli, Long> {
    Optional<Pembeli> findByUsername(String username);  // Pastikan ada metode ini

    Pembeli findByEmail(String email);
}
