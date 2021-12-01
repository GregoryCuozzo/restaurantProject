package com.example.resthony.controller.restaurateur;

import com.example.resthony.services.details.UsersDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Restaurateur controller
 */
@Controller
@RequestMapping("/restaurateur")
public class RestaurateurController {

    @Autowired
    private UsersDetailsServiceImpl service;

    @GetMapping
    public String restaurateurPage(Model model) {

        return "restaurateur/index";
    }




}

