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
    public String all(Model model, RedirectAttributes ra) {
        if(ServiceResto.getAll().isEmpty()){
            ra.addFlashAttribute("messageErreur","Veuillez d'abord cr??er un restaurant pour pouvoir g??rer vos r??servations");
            return"redirect:/restaurateur";
        }

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
            ra.addFlashAttribute("messageErreur", "Vous devez cr??er un horaire pour le restaurant avant de faire une r??servation");
            return "redirect:/restaurateur/reservation/list";
        }
        model.addAttribute("reservations", new CreateReservationIn());
        model.addAttribute("restaurants", ServiceResto.getAll());
        model.addAttribute("users", ServiceUser.getAll());
        model.addAttribute("horaireFiltr??", ServiceHoraire.horaireFiltre());
        return "/restaurateur/reservation/create.html";
    }

    @PostMapping("/create")
    public String createReservation(@Valid @ModelAttribute("reservations") CreateReservationIn createReservationIn, BindingResult bindingResult,Model model, RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("restaurants", ServiceResto.getAll());
            model.addAttribute("users", ServiceUser.getAll());
            model.addAttribute("horaireFiltr??", ServiceHoraire.horaireFiltre());
            return "/restaurateur/reservation/create.html";
        }
        //Info de la r??servation
        String reservationUser = createReservationIn.getUser();
        UserOut userEntity = ServiceUser.findByUsername(reservationUser);
        if (userEntity == null) {
            ra.addFlashAttribute("messageErreur", "Pas d'utilisateur trouv?? ce nom d'utilisateur");
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
            ra.addFlashAttribute("messageErreur", "Probl??me avec la cr??ation de la r??servation.");
            return "redirect:/restaurateur/reservation/list";
        }

        // Envoi de mail
        if (userEntity.getContact().equals("email")) {
            try {
                //Info de la r??servation - r??cup??ration de l'email de l'utilisateur
                String reservationEmail = userEntity.getEmail();

                //Info email
                String emailAdress = reservationEmail;
                String emailSubject = "Merci pour votre r??servation chez " + reservationResto + ".";
                String emailText = "<p>Bonjour madame/monsieur " + reservationName + ",</p>"
                        + "<p>Merci pour votre r??servation chez " + reservationResto + ".</p>"
                        + "<p>Le " + reservationDate + " ?? " + reservationTime + " pour " + reservationNbPersonne + " personnes. </p>"
                        + "<p>Pour annuler votre r??servation, <b><a href=\"\">cliquez-ici</a></b>.</p>"
                        + "<p>Ou rendez-vous sur votre compte Resthony.";
                ServiceEmail.sendEmail(emailAdress, emailSubject, emailText);
            } catch (MessagingException | UnsupportedEncodingException e) {
                ra.addFlashAttribute("messageErreur", "R??servation envoy??e mais probl??me avec l'envoi de l'email de confirmation.");
                return "redirect:/restaurateur/reservation/list";
            }
            ra.addFlashAttribute("message", "R??servation : Un email de confirmation ??t?? envoy?? ?? l'utilisateur.");
            return "redirect:/restaurateur/reservation/list";
        }

        // Envoi de SMS
        if (userEntity.getContact().equals("sms")) {
            try {
                String smsMessage = "Bonjour madame/monsieur " + reservationName + ", " +
                        "Merci pour votre r??servation chez " + reservationResto + ", " +
                        "Le " + reservationDate + " ?? " + reservationTime + " pour " + reservationNbPersonne + " personnes. " +
                        "Pour annuler votre r??servation, rendez-vous sur votre compte Resthony en ligne. ";
                SmsRequest smsRequest = new SmsRequest(userEntity.getPhone(), smsMessage);
                ServiceSms.sendSms(smsRequest);
            } catch (Exception e) {
                ra.addFlashAttribute("messageErreur", "R??servation envoy??e mais probl??me avec l'envoi du SMS de confirmation.");
                return "redirect:/restaurateur/reservation/list";
            }
            ra.addFlashAttribute("message", "R??servation : Un SMS de confirmation ??t?? envoy?? ?? l'utilisateur.");
            return "redirect:/restaurateur/reservation/list";
        }
        ra.addFlashAttribute("message", "R??servation cr????e");
        return "redirect:/restaurateur/reservation/list";
    }


    @GetMapping("/createVisitor")
    public String createVisitor(Model model, RedirectAttributes ra) {
        List<HoraireOut> horaire = ServiceHoraire.getAll();
        if (horaire.isEmpty()){
            ra.addFlashAttribute("messageErreur", "Vous devez cr??er un horaire pour le restaurant avant de faire une r??servation");
            return "redirect:/restaurateur/reservation/list";
        }
        model.addAttribute("visitor", new CreateVisitorIn());
        model.addAttribute("restaurants", ServiceResto.getAll());
        return "/restaurateur/reservation/createVisitor.html";
    }


    @PostMapping("/createVisitor")
    public String createVisitor(@Valid @ModelAttribute("visitor") CreateVisitorIn createVisitorIn, BindingResult bindingResult,Model model, RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("restaurants", ServiceResto.getAll());
            return "/restaurateur/reservation/createVisitor.html";
        }
        try {
            ServiceVisitor.create(createVisitorIn);
        } catch (Exception e) {
            ra.addFlashAttribute("messageErreur", "Probl??me avec la cr??ation de la r??servation.");
            return "redirect:/restaurateur/reservation/list";
        }
        try {
            //Info de la r??servation
            String reservationResto = createVisitorIn.getResto();
            String reservationName = createVisitorIn.getLastname();
            String reservationDate = createVisitorIn.getDate().toString();
            String reservationTime = createVisitorIn.getTime().toString();
            String reservationNbPersonne = createVisitorIn.getNbcouverts().toString();

            //Info email
            String emailAdress = createVisitorIn.getEmail();
            String emailSubject = "Merci pour votre r??servation chez " + reservationResto + ".";
            String emailText = "<p>Bonjour madame/monsieur " + reservationName + ",</p>"
                    + "<p>Merci pour votre r??servation chez " + reservationResto + ".</p>"
                    + "<p>Le " + reservationDate + " ?? " + reservationTime + " pour " + reservationNbPersonne + " personnes. </p>"
                    + "<p>Pour annuler votre r??servation, <b><a href=\"\">cliquez-ici</a></b>.</p>";
            ServiceEmail.sendEmail(emailAdress, emailSubject, emailText);
        } catch (MessagingException | UnsupportedEncodingException e) {
            ra.addFlashAttribute("messageErreur", "R??servation envoy??e mais probl??me avec l'envoie de l'email de confirmation.");
            return "redirect:/restaurateur/reservation/list";
        }
        ra.addFlashAttribute("message", "R??servation : Un email de confirmation ??t?? envoy?? ?? l'utilisateur.");
        return "redirect:/restaurateur/reservation/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteReservation(@PathVariable("id") Long id, RedirectAttributes ra) {
        try {
            Service.delete(id);

        } catch (NotFoundException e) {

        }
        ra.addFlashAttribute("message", "La r??servation a bien ??t?? supprim??e");
        return "redirect:/restaurateur/reservation/list";
    }

    @GetMapping("/deleteVisitor/{id}")
    public String deleteVisitor(@PathVariable("id") Long id, RedirectAttributes ra) {
        try {
            ServiceVisitor.deleteVisitor(id);

        } catch (NotFoundException | UserNotFoundException e) {

        }
        ra.addFlashAttribute("message", "La r??servation a bien ??t?? supprim??e");
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
            ra.addFlashAttribute("messageErreur", "Probl??me avec la modification de la r??servation");
            return "redirect:/restaurateur/reservation/list";
        }
        ra.addFlashAttribute("message", "La r??servation a bien ??t?? modifi??e");

        return "redirect:/restaurateur/reservation/list";
    }

    @GetMapping("/updateVisitor/{id}")
    public String updateVisitor(@PathVariable("id") String id, Model model) {
        model.addAttribute("visitors", ServiceVisitor.get(Long.valueOf(id)));
        model.addAttribute("restaurants", ServiceResto.getAll());

        return "/restaurateur/reservation/updateVisitor.html";

    }

    @PostMapping("/updateVisitor")
    public String updateVisitor(@Valid @ModelAttribute("visitors") PatchVisitorIn patchVisitorIn, BindingResult bindingResult,Model model, RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("restaurants", ServiceResto.getAll());
            return "/restaurateur/reservation/updateVisitor.html";
        }

        try {
            ServiceVisitor.patch(patchVisitorIn.getId(), patchVisitorIn);
        } catch (Exception e) {
            ra.addFlashAttribute("messageErreur", "Probl??me dans la modification de la r??servation.");
            return "redirect:/restaurateur/reservation/list";
        }

        ra.addFlashAttribute("message", "La r??servation a bien ??t?? modifi??e");

        return "redirect:/restaurateur/reservation/list";
    }

}
