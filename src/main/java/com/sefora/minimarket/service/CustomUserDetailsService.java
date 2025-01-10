package com.sefora.minimarket.service;

import com.sefora.minimarket.model.Pembeli;
import com.sefora.minimarket.model.User;
import com.sefora.minimarket.repository.PembeliRepository;
import com.sefora.minimarket.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final PembeliRepository pembeliRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public CustomUserDetailsService(PembeliRepository pembeliRepository, BCryptPasswordEncoder passwordEncoder) {
        this.pembeliRepository = pembeliRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Retrieve the user by their username from the Pembeli table
        Pembeli pembeli = pembeliRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Add ROLE_ prefix to the role name
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + pembeli.getRole().name());

        return new org.springframework.security.core.userdetails.User(
                pembeli.getUsername(),
                pembeli.getPassword(),
                Collections.singletonList(authority)
        );
    }

}

