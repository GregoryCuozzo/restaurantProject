package com.example.resthony.controller.admin;


import com.example.resthony.model.dto.reservation.CreateReservationIn;
import com.example.resthony.model.dto.reservation.PatchReservationIn;
import com.example.resthony.model.dto.visitor.CreateVisitorIn;
import com.example.resthony.model.dto.visitor.PatchVisitorIn;
import com.example.resthony.model.entities.User;
import com.example.resthony.services.principal.*;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;


@Controller
@RequestMapping("/admin/reservation")
public class ReservationControllerAdmin {
    private final ReservationService Service;
    private final RestoService ServiceResto;
    private final UserService ServiceUser;
    private final VisitorService ServiceVisitor;


    public ReservationControllerAdmin(ReservationService service, RestoService serviceResto, UserService serviceUser, VisitorService serviceVisitor) {
        Service = service;
        ServiceResto = serviceResto;
        ServiceUser = serviceUser;
        ServiceVisitor = serviceVisitor;
    }


    @GetMapping("/list")
    public String all(Model model){
        model.addAttribute("reservations",Service.getAll());
        model.addAttribute("restaurants",ServiceResto.getAll());
        model.addAttribute("user",ServiceUser.getAll());
        model.addAttribute("Visitors",ServiceVisitor.getAll());
        return "/admin/reservation/reservations.html";

    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("reservations", new CreateReservationIn());
        model.addAttribute("restaurants",ServiceResto.getAll());
        return "/admin/reservation/create.html";
    }

    @PostMapping("/create")
    public String createReservation(@Valid @ModelAttribute("reservations") CreateReservationIn createReservationIn, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            System.out.println(bindingResult);
            return "/create";

        }

        Service.create(createReservationIn);
        return "redirect:/admin/reservation/list";
    }

    @GetMapping("/createVisitor")
    public String createVisitor(Model model){
        model.addAttribute("visitor",new CreateVisitorIn());
        model.addAttribute("restaurants",ServiceResto.getAll());
        return "/admin/reservation/createVisitor.html";
    }


    @PostMapping("/createVisitor")
    public String createVisitor(@Valid @ModelAttribute("visitors") CreateVisitorIn createVisitorIn, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "/createVisitor";
        }

        ServiceVisitor.create(createVisitorIn);

        return "redirect:/admin/reservation/list";
    }



    @GetMapping("/delete/{id}")
    public String deleteReservation(@PathVariable("id") Long id, RedirectAttributes ra) {
        try {
            Service.delete(id);

        } catch (NotFoundException e) {

        }
        ra.addFlashAttribute("message", "la réservation  a été supprimée ");
        return "redirect:/admin/reservation/list";
    }

    @GetMapping("/deleteVisitor/{id}")
    public String deleteVisitor(@PathVariable("id") Long id, RedirectAttributes ra) {
        try {
            ServiceVisitor.deleteVisitor(id);

        } catch (NotFoundException | UserNotFoundException e) {

        }
        ra.addFlashAttribute("message", "la réservation  a été supprimée ");
        return "redirect:/admin/reservation/list";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") String id, Model model) {
        model.addAttribute("reservations", Service.get(Long.valueOf(id)));
        model.addAttribute("restaurants",ServiceResto.getAll());

        return "/admin/reservation/update.html";
    }

    @PostMapping("/update")
    public String updateReservation(@Valid @ModelAttribute("reservations") PatchReservationIn patchReservationIn, BindingResult bindingResult, RedirectAttributes ra) {
        if(bindingResult.hasErrors()) {
            System.out.println(bindingResult);
            return "/admin/reservation/update.html";
        }

        Service.patch(patchReservationIn.getId(), patchReservationIn);
        ra.addFlashAttribute("message", "la réservation a été modifiée  ");

        return "redirect:/admin/reservation/list";
    }

    @GetMapping("/updateVisitor/{id}")
    public String updateVisitor(@PathVariable("id") String id, Model model) {
        model.addAttribute("visitors", ServiceVisitor.get(Long.valueOf(id)));
        model.addAttribute("restaurants",ServiceResto.getAll());

        return "/admin/reservation/updateVisitor.html";

    }
    @PostMapping("/updateVisitor")
    public String updateVisitor(@Valid @ModelAttribute("visitors") PatchVisitorIn patchVisitorIn, BindingResult bindingResult, RedirectAttributes ra) {
        if(bindingResult.hasErrors()) {
            System.out.println(bindingResult);
            return "/admin/reservation/updateVisitor.html";
        }

        ServiceVisitor.patch(patchVisitorIn.getId(), patchVisitorIn);
        ra.addFlashAttribute("message", "la réservation a été modifiée  ");

        return "redirect:/admin/reservation/list";
    }

}
