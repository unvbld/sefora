package com.sefora.minimarket.service;

import com.sefora.minimarket.dto.LoginDTO;
import com.sefora.minimarket.dto.RegisterDTO;
import com.sefora.minimarket.model.Pembeli;
import com.sefora.minimarket.model.Role;
import com.sefora.minimarket.repository.PembeliRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private PembeliRepository pembeliRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    public String register(RegisterDTO registerDTO) {
        Optional<Pembeli> existingUser = pembeliRepository.findByUsername(registerDTO.getUsername());
        if (existingUser.isPresent()) {
            return "Username sudah digunakan";
        }

        Pembeli pembeli = new Pembeli();
        pembeli.setNama(registerDTO.getNama());
        pembeli.setEmail(registerDTO.getEmail());
        pembeli.setUsername(registerDTO.getUsername());
        pembeli.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        pembeli.setRole(Role.PEMBELI);

        pembeliRepository.save(pembeli);
        return "Registrasi berhasil";
    }

    public boolean login(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword())
        );

        return authentication.isAuthenticated();
    }
}
