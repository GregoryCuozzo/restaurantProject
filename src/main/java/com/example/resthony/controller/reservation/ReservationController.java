package com.example.resthony.controller.reservation;


import com.example.resthony.entities.Reservation;
import com.example.resthony.services.impl.ReservationsDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/reservation")
public class ReservationController {
    @Autowired
    private ReservationsDetailsServiceImpl service;


    @GetMapping
    public String reservationPage(Model model) {

        return "admin/reservations";
    }


    @GetMapping("/reservations")
    public String showReservationList(Model model) {

        List<Reservation> listReservations = service.listAll();
        model.addAttribute("listUsers", listReservations);
        return "admin/reservations";

    }

    @PostMapping("/save")
    public String saveReservation(Reservation reservation, RedirectAttributes ra) {
        service.save(reservation);
        ra.addFlashAttribute("message", "vous avez bien réservé");
        return "/index";
    }


    @PostMapping("edit")
    public String editUser(Reservation reservation, RedirectAttributes ra) {
        service.save(reservation);
        ra.addFlashAttribute("message", "La modification a été réalisée");
        return "redirect:/admin/users";

    }
}
