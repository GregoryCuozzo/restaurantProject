package com.example.resthony.controller;

import com.example.resthony.model.entities.User;
import com.example.resthony.services.principal.UsersDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class RegisterController {

    @Autowired
    private UsersDetailsServiceImpl service;

    @GetMapping("/Register")
    public String registerPage(){
        return "form";
    }

    @PostMapping("/save")
    public String saveUser(User user, RedirectAttributes ra) {
        service.save(user);
        ra.addFlashAttribute("message", "vous vous êtes bien enregistré");
        return "redirect:/login";
    }

    @PostMapping("edit")
    public String editUser(User user, RedirectAttributes ra){
        service.save(user);
        ra.addFlashAttribute("message","La modification a été réalisée");
        return "redirect:/admin/users";

    }


}
