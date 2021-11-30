package com.example.resthony.controller.restaurateur;


import com.example.resthony.model.dto.reservation.CreateReservationIn;
import com.example.resthony.model.dto.reservation.PatchReservationIn;
import com.example.resthony.services.principal.ReservationService;
import com.example.resthony.services.principal.RestoService;
import com.example.resthony.services.principal.UserService;
import javassist.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/restaurateur/reservation")
public class ReservationControllerRestau {
    private final ReservationService Service;
    private final RestoService ServiceResto;
    private final UserService ServiceUser;

    public ReservationControllerRestau(ReservationService service, RestoService serviceResto, UserService serviceUser) {
        Service = service;
        ServiceResto = serviceResto;
        ServiceUser = serviceUser;
    }


    @GetMapping("/list")
    public String all(Model model){
        model.addAttribute("reservations",Service.getAll());
        model.addAttribute("restaurants",ServiceResto.getAll());
        model.addAttribute("user",ServiceUser.getAll());
        return "/restaurateur/reservation/reservations.html";

    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("reservations", new CreateReservationIn());
        model.addAttribute("restaurants",ServiceResto.getAll());
        return "/restaurateur/reservation/create.html";
    }

    @PostMapping("/create")
    public String createResto(@Valid @ModelAttribute("reservations") CreateReservationIn createReservationIn, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "/create";
        }

        Service.create(createReservationIn);

        return "redirect:/restaurateur/reservation/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteResto(@PathVariable("id") Long id, RedirectAttributes ra) {
        try {
            Service.delete(id);

        } catch (NotFoundException e) {

        }
        ra.addFlashAttribute("message", "la réservation  a été supprimée ");
        return "redirect:/restaurateur/reservation/list";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") String id, Model model) {
        model.addAttribute("reservations", Service.get(Long.valueOf(id)));
        return "/restaurateur/reservation/update.html";
    }

    @PostMapping("/update")
    public String updateResto(@Valid @ModelAttribute("reservations") PatchReservationIn patchReservationIn, BindingResult bindingResult, RedirectAttributes ra) {
        if(bindingResult.hasErrors()) {
            return "/update";
        }

        Service.patch(patchReservationIn.getId(), patchReservationIn);
        ra.addFlashAttribute("message", "la réservation a été modifiée  ");

        return "redirect:/restaurateur/reservation/list";
    }

}