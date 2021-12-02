package com.example.resthony.controller.restaurateur;

import com.example.resthony.services.principal.HoraireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@EnableAutoConfiguration
@Controller
@RequestMapping("/restaurateur/horaire")
public class HoraireController {
    private final HoraireService horaireService;

    @Autowired
    public HoraireController(HoraireService horaireService) {
        this.horaireService = horaireService;
    }

    @GetMapping("/list/{id}")
    public String all(@PathVariable("id") Long id, Model model) {
        model.addAttribute("horaires", horaireService.findByRestaurant(id));
        return "/restaurateur/horaire/horaireResto.html";
    }
}
