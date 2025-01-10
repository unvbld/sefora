package com.sefora.minimarket.service;

import com.sefora.minimarket.model.Pembeli;
import com.sefora.minimarket.model.Role;
import com.sefora.minimarket.repository.PembeliRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private PembeliRepository pembeliRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Create user method with email, username, nama, password and role as parameters
    public boolean createUser(String username, String password, String email, String nama, String role) {
        Pembeli user = new Pembeli();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));  // Ensure password is encoded
        user.setEmail(email);
        user.setNama(nama);

        // Ensure that role is properly assigned based on the input
        if (role != null && role.equals("ADMIN")) {
            user.setRole(Role.ADMIN);  // Assign role as ADMIN
        } else {
            user.setRole(Role.PEMBELI);  // Default to PEMBELI if not ADMIN
        }

        pembeliRepository.save(user);
        return true;
    }
}
