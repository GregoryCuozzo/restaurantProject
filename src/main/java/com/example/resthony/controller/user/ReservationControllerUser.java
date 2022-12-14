package com.example.resthony.controller.user;


import com.example.resthony.model.dto.reservation.CreateReservationIn;
import com.example.resthony.model.dto.reservation.PatchReservationIn;
import com.example.resthony.model.dto.user.UserOut;
import com.example.resthony.model.entities.SmsRequest;
import com.example.resthony.model.entities.User;
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
@RequestMapping("/user/reservation")
public class ReservationControllerUser {
    private final ReservationService Service;
    private final RestoService ServiceResto;
    private final UserService ServiceUser;
    private final EmailService ServiceEmail;
    private final SmsService ServiceSms;

    public ReservationControllerUser(ReservationService service, RestoService serviceResto, UserService serviceUser, EmailService serviceEmail, SmsService serviceSms) {
        Service = service;
        ServiceResto = serviceResto;
        ServiceUser = serviceUser;
        ServiceEmail = serviceEmail;
        ServiceSms = serviceSms;
    }


    @GetMapping("/list")
    public String all(Model model) {

        User user = ServiceUser.getCurrentUser();

        model.addAttribute("reservations", Service.findByUser(user));
        model.addAttribute("restaurants", ServiceResto.getAll());
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
    public String createReservation(@Valid @ModelAttribute("reservations") CreateReservationIn createReservationIn, BindingResult bindingResult,Model model, RedirectAttributes ra) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("restaurants", ServiceResto.getAll());

            User user = ServiceUser.getCurrentUser();

            model.addAttribute("user", user.getUsername());

            return "/user/reservation/create.html";
        }

        //Info de la r??servation
        String reservationUser = createReservationIn.getUser();
        UserOut userEntity = ServiceUser.findByUsername(reservationUser);

        if (userEntity == null) {
            ra.addFlashAttribute("messageErreur", "Pas d'utilisateur trouv?? ce nom d'utilisateur. Contactez-nous si le probl??me persiste.");
            return "redirect:/user/reservation/list";
        }

        String reservationResto = createReservationIn.getRestaurant();
        String reservationName = createReservationIn.getUser();
        String reservationDate = createReservationIn.getDate().toString();
        String reservationTime = createReservationIn.getTime().toString();
        String reservationNbPersonne = createReservationIn.getNbcouverts().toString();

        try {
            Service.create(createReservationIn);
        }
        catch(Exception e){
            ra.addFlashAttribute("messageErreur", "Probl??me avec la cr??ation de la r??servation. Veuillez nous contacter. Merci.");
            return "redirect:/user/reservation/list";
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
                ra.addFlashAttribute("messageErreur", "R??servation cr????e avec succ??s mais un probl??me est survenu avec l'envoi de l'email de confirmation. Veuillez nous con");
                return "redirect:/user/reservation/list";
            }
            ra.addFlashAttribute("message", "Merci pour votre r??servation chez "+reservationResto+". Un email de confirmation vous a ??t?? envoy??.");
            return "redirect:/user/reservation/list";
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
                ra.addFlashAttribute("messageErreur", "R??servation cr????e mais probl??me est survenu avec l'envoi du SMS de confirmation.");
                return "redirect:/user/reservation/list";
            }
            ra.addFlashAttribute("message", "Merci pour votre r??servation chez "+reservationResto+". Un SMS de confirmation vous a ??t?? envoy??.");
            return "redirect:/user/reservation/list";
        }

        ra.addFlashAttribute("message", "R??servation cr????e avec succ??s! Merci pour votre r??servation.");
        return "redirect:/user/reservation/list";
    }


    @GetMapping("/delete/{id}")
    public String deleteResto(@PathVariable("id") Long id, RedirectAttributes ra) {
        try {
            Service.delete(id);
        } catch (NotFoundException e) {

        }
        ra.addFlashAttribute("message", "La r??servation a bien ??t?? supprim??e");
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
    public String updateResto(@Valid @ModelAttribute("reservations") PatchReservationIn patchReservationIn, BindingResult bindingResult,Model model, RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("restaurants", ServiceResto.getAll());
            User user = ServiceUser.getCurrentUser();
            model.addAttribute("user", user.getUsername());
            return "/user/reservation/update.html";
        }

        try {
            Service.patch(patchReservationIn.getId(), patchReservationIn);
        }
        catch(Exception e){
            ra.addFlashAttribute("messageErreur", "Un probl??me est survenu avec la modification de votre r??servation, veuillez nous contacter si le probl??me persiste.");
            return "redirect:/user/reservation/list";
        }

        ra.addFlashAttribute("message", "La r??servation a bien ??t?? modifi??e");

        return "redirect:/user/reservation/list";
    }

}
