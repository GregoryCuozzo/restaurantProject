package com.example.resthony.controller.user;

import com.example.resthony.repositories.UserRepository;
import com.example.resthony.services.details.UsersDetailsServiceImpl;
import com.example.resthony.services.principal.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Admin controller
 */
@Controller
@RequestMapping("/user")
public class UtilisateurController {

    private final UserService service;

    public UtilisateurController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public String adminPage(Model model) {

        return "user/index";
    }


    @GetMapping("/profil")
    public String getUser(Model model){

        String username = UserService.getCurrentUser();

        model.addAttribute("User", service.findByUsername(username).getUsername());
        return "user/index.html";

    }



}

