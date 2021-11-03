package com.example.resthony.controller.reservation;


import com.example.resthony.entities.Reservation;
import com.example.resthony.services.impl.ReservationsDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class ReservationController {
    @Autowired
    private ReservationsDetailsServiceImpl service;


    @GetMapping
    public String reservationPage(Model model) {

        return "reservation/index";
    }


    @GetMapping("/reservations")
    public String showReservationList(Model model) {

        List<Reservation> listReservations = service.listAll();
        model.addAttribute("listUsers", listReservations);
        return "admin/users";

    }
}
