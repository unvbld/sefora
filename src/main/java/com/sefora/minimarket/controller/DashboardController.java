package com.sefora.minimarket.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String showDashboard() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            if (authentication.getAuthorities().toString().contains("ADMIN")) {
                return "redirect:/admin/dashboard";  // Redirect to the admin dashboard
            } else if (authentication.getAuthorities().toString().contains("PEMBELI")) {
                return "redirect:/user/dashboard";  // Redirect to user dashboard
            }
        }
        return "redirect:/access-denied";  // Redirect to access-denied page for unauthorized users
    }
}

