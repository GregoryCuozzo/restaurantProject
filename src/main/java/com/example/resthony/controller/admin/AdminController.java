package com.example.resthony.controller.admin;

import com.example.resthony.model.entities.User;

import com.example.resthony.services.details.UsersDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Admin controller
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UsersDetailsServiceImpl service;

    @GetMapping
    public String adminPage(Model model) {

        return "admin/index";
    }




}

