package com.example.resthony.controller.user;


import com.example.resthony.model.dto.reservation.CreateReservationIn;
import com.example.resthony.model.dto.reservation.PatchReservationIn;
import com.example.resthony.model.entities.User;
import com.example.resthony.repositories.UserRepository;
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
@RequestMapping("/user/reservation")
public class ReservationControllerUser {
    private final ReservationService Service;
    private final RestoService ServiceResto;
    private final UserService ServiceUser;

    public ReservationControllerUser(ReservationService service, RestoService serviceResto, UserService serviceUser) {
        Service = service;
        ServiceResto = serviceResto;
        ServiceUser = serviceUser;
    }


    @GetMapping("/list")
    public String all(Model model) {

        User user = ServiceUser.getCurrentUser();
        Long id = user.getId();
        model.addAttribute("reservations", Service.findByUser(user));

        model.addAttribute("restaurants", ServiceResto.getAll());
//        model.addAttribute("user",ServiceUser.getAll());
        return "/user/reservation/list.html";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("reservations", new CreateReservationIn());
        model.addAttribute("restaurants", ServiceResto.getAll());

        User user = ServiceUser.getCurrentUser();
        model.addAttribute("user", user.getUsername());

        return "/user/reservation/create.html";
    }

    @PostMapping("/create")
    public String createReservation(@Valid @ModelAttribute("reservations") CreateReservationIn createReservationIn, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/create";
        }
        Service.create(createReservationIn);
        return "redirect:/user/reservation/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteResto(@PathVariable("id") Long id, RedirectAttributes ra) {
        try {
            Service.delete(id);

        } catch (NotFoundException e) {

        }
        ra.addFlashAttribute("message", "la réservation  a été supprimée ");
        return "redirect:/user/reservation/list";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") String id, Model model) {
        model.addAttribute("reservations", Service.get(Long.valueOf(id)));
        model.addAttribute("restaurants", ServiceResto.getAll());
        User user = ServiceUser.getCurrentUser();
        model.addAttribute("user", user.getUsername());

        return "/user/reservation/update.html";
    }

    @PostMapping("/update")
    public String updateResto(@Valid @ModelAttribute("reservations") PatchReservationIn patchReservationIn, BindingResult bindingResult, RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return "/update";
        }

        Service.patch(patchReservationIn.getId(), patchReservationIn);
        ra.addFlashAttribute("message", "la réservation a été modifiée  ");

        return "redirect:/user/reservation/list";
    }

}
