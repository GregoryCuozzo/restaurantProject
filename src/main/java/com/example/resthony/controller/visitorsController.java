package com.example.resthony.controller;

import com.example.resthony.services.principal.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

public class visitorsController {
    @Controller
    @RequestMapping("/user")
    public class UtilisateurController {

        private final UserService service;

        public UtilisateurController(UserService service) {
            this.service = service;
        }

        @GetMapping
        public String userPage(Model model) {

            return "/public/visitor.html";

        }

    }
}
