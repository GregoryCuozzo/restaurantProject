package com.example.resthony.controller.user;

import com.example.resthony.model.dto.user.PatchUserIn;
import com.example.resthony.repositories.UserRepository;
import com.example.resthony.services.details.UsersDetailsServiceImpl;
import com.example.resthony.services.principal.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

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
    public String userPage(Model model) {

        return "user/index.html";
    }


    @GetMapping("/profil")
    //
    public String getUser(Model model){
        model.addAttribute("Username", service.getCurrentUser().getUsername());
        return "user/profil.html";

    }





}

