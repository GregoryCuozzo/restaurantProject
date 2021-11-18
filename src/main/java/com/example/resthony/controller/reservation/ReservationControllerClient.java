package com.example.resthony.controller.reservation;


import com.example.resthony.entities.Reservation;
import com.example.resthony.entities.User;
import com.example.resthony.services.impl.ReservationsDetailsServiceImpl;
import com.example.resthony.services.impl.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/user/reservation")
public class ReservationControllerClient {
    @Autowired
    private ReservationsDetailsServiceImpl service;


    @GetMapping
    public String reservationPage(Model model) {
        return "/user/reservations";

    }


    @PostMapping("/save")
    public String saveReservation(Reservation reservation, RedirectAttributes ra) {
        service.save(reservation);
        ra.addFlashAttribute("message", "vous avez bien réservé");
        return "/index";
    }


    @PostMapping("edit")
    public String editReservation(Reservation reservation, RedirectAttributes ra) {
        service.save(reservation);
        ra.addFlashAttribute("message", "La modification a été réalisée");
        return "redirect:/user/reservations";

    }

    @GetMapping("/delete/{id}")
    public String deleteReservation(@PathVariable("id") Integer id, RedirectAttributes ra) {
        try {
            service.delete(id);

        } catch (UserNotFoundException e) {

        }
        ra.addFlashAttribute("message", "la Reservationr a été suppriméé ");
        return "redirect:/admin/reservations";
    }
}