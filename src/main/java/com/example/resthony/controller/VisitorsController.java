package com.example.resthony.controller;

import com.example.resthony.model.dto.visitor.CreateVisitorIn;
import com.example.resthony.services.principal.EmailService;
import com.example.resthony.services.principal.RestoService;
import com.example.resthony.services.principal.VisitorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;


@Controller
@RequestMapping("/visitor")
public class VisitorsController {
    private final VisitorService ServiceVisitor;
    private final RestoService ServiceResto;
    private final EmailService ServiceEmail;

    public VisitorsController(RestoService serviceResto, VisitorService serviceVisitor, EmailService serviceEmail) {
        ServiceVisitor = serviceVisitor;
        ServiceResto = serviceResto;
        ServiceEmail = serviceEmail;
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("createVisitorIn", new CreateVisitorIn());
        model.addAttribute("restaurants", ServiceResto.getAll());
        return "/public/visitor.html";
    }


    @PostMapping("/create")
    public String createVisitor(@Valid @ModelAttribute("createVisitorIn") CreateVisitorIn createVisitorIn, BindingResult bindingResult, Model model, RedirectAttributes ra) throws MessagingException, UnsupportedEncodingException {

        if (bindingResult.hasErrors()) {
            model.addAttribute("restaurants", ServiceResto.getAll());
            return "/public/visitor.html";
        }

        try {
            ServiceVisitor.create(createVisitorIn);
        } catch (Exception e) {
            ra.addFlashAttribute("messageErreur", "Problème dans la création de votre réservation. Nous nous en excusons. Veuillez nous contacter.");
            return "redirect:/";
        }

        //Envoi de l'email de confirmation
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
            ra.addFlashAttribute("messageErreur", "Problème avec l'envoie de l'email de confirmation. Contactez-nous pour plus d'informations.");
        }
        ra.addFlashAttribute("message", "Un email de confirmation vous a été envoyé.");
        return "redirect:/";

    }


}
