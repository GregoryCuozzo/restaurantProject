package com.example.resthony.controller;
import com.example.resthony.model.dto.user.CreateUserIn;
import com.example.resthony.model.entities.SmsRequest;
import com.example.resthony.services.principal.EmailService;
import com.example.resthony.services.principal.SmsService;
import com.example.resthony.services.principal.UserService;
import com.example.resthony.utils.BCryptManagerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.Objects;


@Controller
@RequestMapping("/register")
public class RegisterController {

    private final SmsService ServiceSms;
    private final UserService service;
    private final EmailService ServiceEmail;

    @Autowired
    public RegisterController(SmsService serviceSms, UserService service, EmailService serviceEmail) {
        ServiceSms = serviceSms;
        this.service = service;
        ServiceEmail = serviceEmail;
    }

    @GetMapping

    public String registerPage(CreateUserIn createUserIn) {
        return "/public/register";
    }


    @PostMapping("/create")
    public String createUser(@ModelAttribute @Valid CreateUserIn createUserIn, BindingResult bindingResult,Model model, RedirectAttributes ra
    ) {
        if (bindingResult.hasErrors()) {
            return "/public/register.html";
        }

        String message = service.checkDuplicateCreate(createUserIn);

        if (!message.equals("")) {
            ra.addFlashAttribute("messageErreur", message);
            return "redirect:/register";
        }

        // ---------------- AUTO LOGIN APRES REGISTER A FAIRE --------------------------------


        String restPasswordValue = BCryptManagerUtil.passwordEncoder().encode(createUserIn.getPassword());
        createUserIn.setPassword(restPasswordValue);

        try {
            service.register(createUserIn);
        } catch (Exception e) {
            ra.addFlashAttribute("messageErreur", "Il y a eu un problème dans la création de votre compte.");
            return "redirect:/";
        }


        if (createUserIn.getContact().equals("email")) {
            try {
                //Info sur le user

                String registerName = createUserIn.getLastname();

                //Info email
                String emailAdress = createUserIn.getEmail();
                String emailSubject = "Compte crée chez Resthony.";
                String emailText = "<p>Bonjour monsieur " + registerName + ",</p>"
                        + "<p>Merci d'avoir crée un compte chez nous, vous pouvez maintenant effectuer vos réservations plus facilement en vous connectant.</p>"
                        + "<p>N'hésitez pas à nous contacter si vous avez des questions.</p>";
                ServiceEmail.sendEmail(emailAdress, emailSubject, emailText);

            } catch (MessagingException | UnsupportedEncodingException e) {
                ra.addFlashAttribute("messageErreur", "Problème avec l'envoie de l'email de confirmation du compte.");
                return "redirect:/";
            }
            ra.addFlashAttribute("message", "Un email de confirmation de création du compte vous a été envoyé.");
            return "redirect:/";
        }

        if (createUserIn.getContact().equals("sms"))
        {
            try {
                String registerName = createUserIn.getLastname();
                String smsMessage = "Bonjour monsieur " + registerName + "," +
                        " Merci d'avoir crée un compte chez nous, vous pouvez maintenant effectuer vos réservations plus facilement en vous connectant." +
                        " N'hésitez pas à nous contacter si vous avez des questions.";
                SmsRequest smsRequest = new SmsRequest(createUserIn.getPhone(), smsMessage);
                ServiceSms.sendSms(smsRequest);
            } catch (Exception e) {
                ra.addFlashAttribute("messageErreur", "Compte crée mais problème avec l'envoie de l'sms de confirmation du compte.");
                return "redirect:/";
            }
            ra.addFlashAttribute("message", "Un SMS de confirmation de création du compte vous a été envoyé.");
            return "redirect:/";
        }

        ra.addFlashAttribute("message", "Votre compte a bien été crée.");
        return "redirect:/";

    }

}
