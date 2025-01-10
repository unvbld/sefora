package com.sefora.minimarket.service;

import com.sefora.minimarket.model.Pembeli;
import com.sefora.minimarket.repository.PembeliRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PembeliService {

    @Autowired
    private PembeliRepository pembeliRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Simpan data pembeli baru setelah mengenkripsi password.
     *
     * @param pembeli Data pembeli
     * @return Pembeli yang telah disimpan
     */
    public Pembeli savePembeli(Pembeli pembeli) {
        // Hash password sebelum menyimpan
        pembeli.setPassword(passwordEncoder.encode(pembeli.getPassword()));
        return pembeliRepository.save(pembeli);
    }

    /**
     * Cari pembeli berdasarkan username.
     *
     * @param username Username pembeli
     * @return Pembeli jika ditemukan, atau null jika tidak ditemukan
     */
    public Pembeli findPembeliByUsername(String username) {
        return pembeliRepository.findByUsername(username).orElse(null);
    }

    /**
     * Autentikasi pembeli berdasarkan email dan password.
     *
     * @param email    Email pembeli
     * @param password Password pembeli (plain text)
     * @return Pembeli jika autentikasi berhasil, atau null jika gagal
     */
    public Pembeli authenticate(String email, String password) {
        Optional<Pembeli> pembeliOptional = Optional.ofNullable(pembeliRepository.findByEmail(email));

        if (pembeliOptional.isPresent()) {
            Pembeli pembeli = pembeliOptional.get();
            // Cocokkan password hash menggunakan PasswordEncoder
            if (passwordEncoder.matches(password, pembeli.getPassword())) {
                return pembeli;
            }
        }
        return null;
    }
}
