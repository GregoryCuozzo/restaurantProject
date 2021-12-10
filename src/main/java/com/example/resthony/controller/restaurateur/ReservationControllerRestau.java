package com.example.resthony.controller.restaurateur;


import com.example.resthony.model.dto.horaire.HoraireOut;
import com.example.resthony.model.dto.reservation.CreateReservationIn;
import com.example.resthony.model.dto.reservation.PatchReservationIn;
import com.example.resthony.model.dto.user.UserOut;
import com.example.resthony.model.dto.visitor.CreateVisitorIn;
import com.example.resthony.model.dto.visitor.PatchVisitorIn;
import com.example.resthony.model.entities.SmsRequest;
import com.example.resthony.services.principal.*;
import javassist.NotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/restaurateur/reservation")
public class ReservationControllerRestau {
    private final ReservationService Service;
    private final RestoService ServiceResto;
    private final UserService ServiceUser;
    private final VisitorService ServiceVisitor;
    private final EmailService ServiceEmail;
    private final SmsService ServiceSms;
    private final HoraireService ServiceHoraire;

    public ReservationControllerRestau(ReservationService service, RestoService serviceResto, UserService serviceUser, VisitorService serviceVisitor, EmailService serviceEmail, SmsService serviceSms, com.example.resthony.services.principal.HoraireService serviceHoraire) {
        Service = service;
        ServiceResto = serviceResto;
        ServiceUser = serviceUser;
        ServiceVisitor = serviceVisitor;
        ServiceEmail = serviceEmail;
        ServiceSms = serviceSms;
        ServiceHoraire = serviceHoraire;
    }


    @GetMapping("/list")
    public String all(Model model) {
        model.addAttribute("reservations", Service.getAll());
        model.addAttribute("restaurants", ServiceResto.getAll());
        model.addAttribute("user", ServiceUser.getAll());
        model.addAttribute("Visitors", ServiceVisitor.getAll());
        return "/restaurateur/reservation/reservations.html";

    }

    @GetMapping("/create")
    public String create(Model model, RedirectAttributes ra) {
        List<HoraireOut> horaire = ServiceHoraire.getAll();
        if (horaire.isEmpty()){
            ra.addFlashAttribute("messageErreur", "Vous devez créer un horaire pour le restaurant avant de faire une réservation");
            return "redirect:/restaurateur/reservation/list";
        }
        model.addAttribute("reservations", new CreateReservationIn());
        model.addAttribute("restaurants", ServiceResto.getAll());
        model.addAttribute("users", ServiceUser.getAll());
        model.addAttribute("horaireFiltré", ServiceHoraire.horaireFiltre());
        return "/restaurateur/reservation/create.html";
    }

    @PostMapping("/create")
    public String createReservation(@Valid @ModelAttribute("reservations") CreateReservationIn createReservationIn, BindingResult bindingResult, RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return "/create";
        }
        //Info de la réservation
        String reservationUser = createReservationIn.getUser();
        UserOut userEntity = ServiceUser.findByUsername(reservationUser);
        if (userEntity == null) {
            ra.addFlashAttribute("messageErreur", "Pas d'utilisateur trouvé ce nom d'utilisateur");
            return "redirect:/restaurateur/reservation/list";
        }
        String reservationResto = createReservationIn.getRestaurant();
        String reservationName = createReservationIn.getUser();
        String reservationDate = createReservationIn.getDate().toString();
        String reservationTime = createReservationIn.getTime().toString();
        String reservationNbPersonne = createReservationIn.getNbcouverts().toString();
        try {
            Service.create(createReservationIn);
        } catch (Exception e) {
            ra.addFlashAttribute("messageErreur", "Problème avec la création de la réservation.");
            return "redirect:/restaurateur/reservation/list";
        }

        // Envoi de mail
        if (userEntity.getContact().equals("email")) {
            try {
                //Info de la réservation - récupération de l'email de l'utilisateur
                String reservationEmail = userEntity.getEmail();

                //Info email
                String emailAdress = reservationEmail;
                String emailSubject = "Merci pour votre réservation chez " + reservationResto + ".";
                String emailText = "<p>Bonjour madame/monsieur " + reservationName + ",</p>"
                        + "<p>Merci pour votre réservation chez " + reservationResto + ".</p>"
                        + "<p>Le " + reservationDate + " à " + reservationTime + " pour " + reservationNbPersonne + " personnes. </p>"
                        + "<p>Pour annuler votre réservation, <b><a href=\"\">cliquez-ici</a></b>.</p>"
                        + "<p>Ou rendez-vous sur votre compte Resthony.";
                ServiceEmail.sendEmail(emailAdress, emailSubject, emailText);
            } catch (MessagingException | UnsupportedEncodingException e) {
                ra.addFlashAttribute("messageErreur", "Réservation envoyée mais problème avec l'envoi de l'email de confirmation.");
                return "redirect:/restaurateur/reservation/list";
            }
            ra.addFlashAttribute("message", "Réservation : Un email de confirmation été envoyé à l'utilisateur.");
            return "redirect:/restaurateur/reservation/list";
        }

        // Envoi de SMS
        if (userEntity.getContact().equals("sms")) {
            try {
                String smsMessage = "Bonjour madame/monsieur " + reservationName + ", " +
                        "Merci pour votre réservation chez " + reservationResto + ", " +
                        "Le " + reservationDate + " à " + reservationTime + " pour " + reservationNbPersonne + " personnes. " +
                        "Pour annuler votre réservation, rendez-vous sur votre compte Resthony en ligne. ";
                SmsRequest smsRequest = new SmsRequest(userEntity.getPhone(), smsMessage);
                ServiceSms.sendSms(smsRequest);
            } catch (Exception e) {
                ra.addFlashAttribute("messageErreur", "Réservation envoyée mais problème avec l'envoi du SMS de confirmation.");
                return "redirect:/restaurateur/reservation/list";
            }
            ra.addFlashAttribute("message", "Réservation : Un SMS de confirmation été envoyé à l'utilisateur.");
            return "redirect:/restaurateur/reservation/list";
        }
        ra.addFlashAttribute("message", "Réservation créée");
        return "redirect:/restaurateur/reservation/list";
    }


    @GetMapping("/createVisitor")
    public String createVisitor(Model model, RedirectAttributes ra) {
        List<HoraireOut> horaire = ServiceHoraire.getAll();
        if (horaire.isEmpty()){
            ra.addFlashAttribute("messageErreur", "Vous devez créer un horaire pour le restaurant avant de faire une réservation");
            return "redirect:/restaurateur/reservation/list";
        }
        model.addAttribute("visitor", new CreateVisitorIn());
        model.addAttribute("restaurants", ServiceResto.getAll());
        return "/restaurateur/reservation/createVisitor.html";
    }


    @PostMapping("/createVisitor")
    public String createVisitor(@Valid @ModelAttribute("visitors") CreateVisitorIn createVisitorIn, BindingResult bindingResult, RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return "/createVisitor";
        }
        try {
            ServiceVisitor.create(createVisitorIn);
        } catch (Exception e) {
            ra.addFlashAttribute("messageErreur", "Problème avec la création de la réservation.");
            return "redirect:/restaurateur/reservation/list";
        }
        try {
            //Info de la réservation
            String reservationResto = createVisitorIn.getResto();
            String reservationName = createVisitorIn.getLastname();
            String reservationDate = createVisitorIn.getDate().toString();
            String reservationTime = createVisitorIn.getTime().toString();
            String reservationNbPersonne = createVisitorIn.getNbcouverts().toString();

            //Info email
            String emailAdress = createVisitorIn.getEmail();
            String emailSubject = "Merci pour votre réservation chez " + reservationResto + ".";
            String emailText = "<p>Bonjour madame/monsieur " + reservationName + ",</p>"
                    + "<p>Merci pour votre réservation chez " + reservationResto + ".</p>"
                    + "<p>Le " + reservationDate + " à " + reservationTime + " pour " + reservationNbPersonne + " personnes. </p>"
                    + "<p>Pour annuler votre réservation, <b><a href=\"\">cliquez-ici</a></b>.</p>";
            ServiceEmail.sendEmail(emailAdress, emailSubject, emailText);
        } catch (MessagingException | UnsupportedEncodingException e) {
            ra.addFlashAttribute("messageErreur", "Réservation envoyée mais problème avec l'envoie de l'email de confirmation.");
            return "redirect:/restaurateur/reservation/list";
        }
        ra.addFlashAttribute("message", "Réservation : Un email de confirmation été envoyé à l'utilisateur.");
        return "redirect:/restaurateur/reservation/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteReservation(@PathVariable("id") Long id, RedirectAttributes ra) {
        try {
            Service.delete(id);

        } catch (NotFoundException e) {

        }
        ra.addFlashAttribute("message", "La réservation a bien été supprimée");
        return "redirect:/restaurateur/reservation/list";
    }

    @GetMapping("/deleteVisitor/{id}")
    public String deleteVisitor(@PathVariable("id") Long id, RedirectAttributes ra) {
        try {
            ServiceVisitor.deleteVisitor(id);

        } catch (NotFoundException | UserNotFoundException e) {

        }
        ra.addFlashAttribute("message", "La réservation a bien été supprimée");
        return "redirect:/restaurateur/reservation/list";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") String id, Model model) {
        model.addAttribute("reservations", Service.get(Long.valueOf(id)));
        model.addAttribute("restaurants", ServiceResto.getAll());
        model.addAttribute("users", ServiceUser.getAll());
        return "/restaurateur/reservation/update.html";
    }

    @PostMapping("/update")
    public String updateResto(@Valid @ModelAttribute("reservations") PatchReservationIn patchReservationIn, BindingResult bindingResult, RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return "/update";
        }
        try {
            Service.patch(patchReservationIn.getId(), patchReservationIn);
        } catch (Exception e) {
            ra.addFlashAttribute("messageErreur", "Problème avec la modification de la réservation");
            return "redirect:/restaurateur/reservation/list";
        }
        ra.addFlashAttribute("message", "La réservation a bien été modifiée");

        return "redirect:/restaurateur/reservation/list";
    }

    @GetMapping("/updateVisitor/{id}")
    public String updateVisitor(@PathVariable("id") String id, Model model) {
        model.addAttribute("visitors", ServiceVisitor.get(Long.valueOf(id)));
        model.addAttribute("restaurants", ServiceResto.getAll());

        return "/restaurateur/reservation/updateVisitor.html";

    }

    @PostMapping("/updateVisitor")
    public String updateVisitor(@Valid @ModelAttribute("visitors") PatchVisitorIn patchVisitorIn, BindingResult bindingResult, RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return "/restaurateur/reservation/updateVisitor.html";
        }

        try {
            ServiceVisitor.patch(patchVisitorIn.getId(), patchVisitorIn);
        } catch (Exception e) {
            ra.addFlashAttribute("messageErreur", "Problème dans la modification de la réservation.");
            return "redirect:/restaurateur/reservation/list";
        }

        ra.addFlashAttribute("message", "La réservation a bien été modifiée");

        return "redirect:/restaurateur/reservation/list";
    }

}
