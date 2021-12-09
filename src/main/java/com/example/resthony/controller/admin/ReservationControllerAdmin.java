package com.example.resthony.controller.admin;


import com.example.resthony.model.dto.reservation.CreateReservationIn;
import com.example.resthony.model.dto.reservation.PatchReservationIn;
import com.example.resthony.model.dto.user.UserOut;
import com.example.resthony.model.dto.visitor.CreateVisitorIn;
import com.example.resthony.model.dto.visitor.PatchVisitorIn;
import com.example.resthony.services.principal.*;
import javassist.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;


@Controller
@RequestMapping("/admin/reservation")
public class ReservationControllerAdmin {
    private final ReservationService Service;
    private final RestoService ServiceResto;
    private final UserService ServiceUser;
    private final VisitorService ServiceVisitor;
    private final EmailService ServiceEmail;


    public ReservationControllerAdmin(ReservationService service, RestoService serviceResto, UserService serviceUser, VisitorService serviceVisitor, EmailService serviceEmail) {
        Service = service;
        ServiceResto = serviceResto;
        ServiceUser = serviceUser;
        ServiceVisitor = serviceVisitor;
        ServiceEmail = serviceEmail;
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
        model.addAttribute("users",ServiceUser.getAll());
        return "/admin/reservation/create.html";
    }

    @PostMapping("/create")
    public String createReservation(@Valid @ModelAttribute("reservations") CreateReservationIn createReservationIn, BindingResult bindingResult,RedirectAttributes ra) {
        if(bindingResult.hasErrors()) {
            return "/create";

        }

        Service.create(createReservationIn);
        try {
            //Info de la réservation - récupération de l'email de l'utilisateur
            String reservationUser = createReservationIn.getUser();
            UserOut userEntity = ServiceUser.findByUsername(reservationUser);
            String reservationEmail = userEntity.getEmail();
            //Info de la réservation
            String reservationResto = createReservationIn.getRestaurant();
            String reservationName = createReservationIn.getUser();
            String reservationDate = createReservationIn.getDate().toString();
            String reservationTime = createReservationIn.getTime().toString();
            String reservationNbPersonne = createReservationIn.getNbcouverts().toString();
            //Info email
            String emailAdress = reservationEmail;
            String emailSubject = "Merci pour votre réservation chez " +reservationResto+".";
            String emailText = "<p>Bonjour monsieur "+reservationName+",</p>"
                    + "<p>Merci pour votre réservation chez " +reservationResto+".</p>"
                    + "<p>Le " + reservationDate + " à " + reservationTime + " pour " + reservationNbPersonne + " personnes. </p>"
                    + "<p>Pour annuler votre réservation, <b><a href=\"\">cliquez-ici</a></b>.</p>"
                    + "<p>Ou rendez-vous sur votre compte Resthony.";
            ServiceEmail.sendEmail(emailAdress, emailSubject, emailText);
        }
        catch (MessagingException | UnsupportedEncodingException e){
            ra.addFlashAttribute("messageErreur", "Réservation envoyée mais problème avec l'envoie de l'email de confirmation.");
            return "redirect:/admin/reservation/list";
        }
        ra.addFlashAttribute("message", "Réservation : Un email de confirmation été envoyé à l'utilisateur.");
        return "redirect:/admin/reservation/list";
    }

    @GetMapping("/createVisitor")
    public String createVisitor(Model model){
        model.addAttribute("visitor",new CreateVisitorIn());
        model.addAttribute("restaurants",ServiceResto.getAll());
        return "/admin/reservation/createVisitor.html";
    }


    @PostMapping("/createVisitor")
    public String createVisitor(@Valid @ModelAttribute("visitors") CreateVisitorIn createVisitorIn, BindingResult bindingResult, RedirectAttributes ra) {
        if(bindingResult.hasErrors()) {
            return "/createVisitor";
        }

        ServiceVisitor.create(createVisitorIn);
        try {
            //Info de la réservation
            String reservationResto = createVisitorIn.getResto();
            String reservationName = createVisitorIn.getLastname();
            String reservationDate = createVisitorIn.getDate().toString();
            String reservationTime = createVisitorIn.getTime().toString();
            String reservationNbPersonne = createVisitorIn.getNbcouverts().toString();

            //Info email
            String emailAdress = createVisitorIn.getEmail();
            String emailSubject = "Merci pour votre réservation chez " +reservationResto+".";
            String emailText = "<p>Bonjour monsieur "+reservationName+",</p>"
                    + "<p>Merci pour votre réservation chez " +reservationResto+".</p>"
                    + "<p>Le " + reservationDate + " à " + reservationTime + " pour " + reservationNbPersonne + " personnes. </p>"
                    + "<p>Pour annuler votre réservation, <b><a href=\"\">cliquez-ici</a></b>.</p>";
            ServiceEmail.sendEmail(emailAdress, emailSubject, emailText);
        }
        catch (MessagingException | UnsupportedEncodingException e){
            ra.addFlashAttribute("messageErreur", "Réservation envoyée mais problème avec l'envoie de l'email de confirmation.");
            return "redirect:/admin/reservation/list";
        }
        ra.addFlashAttribute("message", "Réservation : Un email de confirmation été envoyé à l'utilisateur.");
        return "redirect:/admin/reservation/list";
    }


    @GetMapping("/delete/{id}")
    public String deleteReservation(@PathVariable("id") Long id, RedirectAttributes ra) {
        try {
            Service.delete(id);

        } catch (NotFoundException e) {

        }
        ra.addFlashAttribute("message", "La réservation a bien été supprimée");
        return "redirect:/admin/reservation/list";
    }

    @GetMapping("/deleteVisitor/{id}")
    public String deleteVisitor(@PathVariable("id") Long id, RedirectAttributes ra) {
        try {
            ServiceVisitor.deleteVisitor(id);

        } catch (NotFoundException | UserNotFoundException e) {

        }
        ra.addFlashAttribute("message", "La réservation a bien été supprimée");
        return "redirect:/admin/reservation/list";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") String id, Model model) {
        model.addAttribute("reservations", Service.get(Long.valueOf(id)));
        model.addAttribute("restaurants",ServiceResto.getAll());
        model.addAttribute("users",ServiceUser.getAll());

        return "/admin/reservation/update.html";
    }

    @PostMapping("/update")
    public String updateReservation(@Valid @ModelAttribute("reservations") PatchReservationIn patchReservationIn, BindingResult bindingResult, RedirectAttributes ra) {
        if(bindingResult.hasErrors()) {
            System.out.println(bindingResult);
            return "/admin/reservation/update.html";
        }

        Service.patch(patchReservationIn.getId(), patchReservationIn);
        ra.addFlashAttribute("message", "La réservation a bien été modifiée");

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
        ra.addFlashAttribute("message", "La réservation a bien été modifiée");

        return "redirect:/admin/reservation/list";
    }

}
