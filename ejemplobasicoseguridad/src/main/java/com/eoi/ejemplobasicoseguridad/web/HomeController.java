package com.eoi.ejemplobasicoseguridad.web;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String getHomePage1() {
        return "homePage";
    }
    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }
    @GetMapping("/logout")
    public String getLogoutPage() {
        return "logout";
    }
    @GetMapping("/home")
    public String getHomePage() {
        return "homePage";
    }

    @GetMapping("/welcome")
    public String getWelcomePage() {
        return "welcomePage";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String getAdminPage() {
        return "adminPage";
    }

    @GetMapping("/emp")
    public String getEmployeePage() {
        return "empPage";
    }

    @GetMapping("/mgr")
    public String getManagerPage() {
        return "mgrPage";
    }

    @GetMapping("/hr")
    public String getHrPage() {
        return "hrPage";
    }

    @GetMapping("/common")
    public String getCommonPage() {
        return "commonPage";
    }

        @GetMapping("/accessDenied")
    public String getAccessDeniedPage() {
        return "accessDeniedPage";
    }
}