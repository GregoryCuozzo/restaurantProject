package com.example.resthony.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/*
* Controller pour la page admin
* */
@Controller
@RequestMapping("/login")
public class LoginController {
    @GetMapping
    public String loginPage() {
        return "/public/login";
    }

    @GetMapping("/error")
    public String loginErrorPage(Model model) {
        model.addAttribute("loginError", true);
        return "/public/login";
    }

}
