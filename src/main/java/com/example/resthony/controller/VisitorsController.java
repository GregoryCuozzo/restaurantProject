package com.example.resthony.controller;


import com.example.resthony.model.dto.reservation.CreateReservationIn;
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
    private final ReservationService Service;
    private final RestoService ServiceResto;

    public VisitorsController(ReservationService service, RestoService serviceResto,VisitorService serviceVisitor){
        ServiceVisitor = serviceVisitor;
        ServiceResto = serviceResto;
        Service = service;


    }

    @GetMapping("/create")
    public String create(Model model){
        model.addAttribute("reservations", new CreateReservationIn());
        model.addAttribute("restaurants",ServiceResto.getAll());

        return "/public/visitor.html";
    }


    @PostMapping("/create")
    public String createResto(@Valid @ModelAttribute("reservations") CreateReservationIn createReservationIn, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "/create";
        }

        Service.create(createReservationIn);

        return "redirect:/public/login";
    }

}
