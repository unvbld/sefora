package com.sefora.minimarket.config;

import com.sefora.minimarket.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService, PasswordEncoder passwordEncoder) {
        this.customUserDetailsService = customUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable() // CSRF tidak digunakan
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/login", "/register", "/error").permitAll() // URL publik
                        .requestMatchers("/admin/**").hasRole("ADMIN") // URL admin
                        .requestMatchers("/user/**").hasRole("PEMBELI") // URL user dengan role PEMBELI
                        .anyRequest().authenticated() // Semua request lainnya perlu autentikasi
                )
                .formLogin(form -> form
                        .loginPage("/login") // Custom login page
                        .permitAll()
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .loginProcessingUrl("/login") // Proses login di endpoint ini
                        .defaultSuccessUrl("/user/dashboard", true) // Redirect setelah login sukses
                        .failureUrl("/login?error=true") // Redirect jika login gagal
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout") // Redirect setelah logout
                        .permitAll()
                )
                .exceptionHandling()
                .accessDeniedPage("/access-denied"); // Halaman untuk akses ditolak

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder);
        return authenticationManagerBuilder.build();
    }
}
