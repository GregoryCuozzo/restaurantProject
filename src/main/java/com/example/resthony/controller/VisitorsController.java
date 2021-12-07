package com.example.resthony.controller;


import com.example.resthony.model.dto.reservation.CreateReservationIn;
import com.example.resthony.model.dto.visitor.CreateVisitorIn;
import com.example.resthony.services.principal.ReservationService;
import com.example.resthony.services.principal.RestoService;
import com.example.resthony.services.principal.VisitorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;


@Controller
@RequestMapping("/visitor")
public class VisitorsController {
    private final VisitorService ServiceVisitor;
    private final RestoService ServiceResto;

    public VisitorsController(RestoService serviceResto,VisitorService serviceVisitor){
        ServiceVisitor = serviceVisitor;
        ServiceResto = serviceResto;
    }

    @GetMapping("/create")
    public String create(Model model){
        model.addAttribute("visitor",new CreateVisitorIn());
        model.addAttribute("restaurants",ServiceResto.getAll());
        return "/public/visitor.html";
    }


    @PostMapping("/create")
    public String createVisitor(@Valid @ModelAttribute("visitors") CreateVisitorIn createVisitorIn, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "/create";
        }

        ServiceVisitor.create(createVisitorIn);

        return "redirect:/";
    }

}
