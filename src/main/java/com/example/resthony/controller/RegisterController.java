package com.example.resthony.controller;
import com.example.resthony.model.dto.user.CreateUserIn;
import com.example.resthony.model.dto.visitor.CreateVisitorIn;
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

    public String registerPage(Model model) {
        model.addAttribute("createUserIn", new CreateUserIn());
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
            ra.addFlashAttribute("messageErreur", "Il y a eu un probl??me dans la cr??ation de votre compte.");
            return "redirect:/";
        }

        if (createUserIn.getContact().equals("email")) {
            try {
                //Info sur le user

                String registerName = createUserIn.getLastname();

                //Info email
                String emailAdress = createUserIn.getEmail();
                String emailSubject = "Compte cr??e chez Resthony.";
                String emailText = "<p>Bonjour madame/monsieur " + registerName + ",</p>"
                        + "<p>Merci d'avoir cr??e un compte chez Resthony, vous pouvez maintenant effectuer vos r??servations plus facilement en vous connectant.</p>"
                        + "<p>N'h??sitez pas ?? nous contacter si vous avez des questions.</p>";
                message = ServiceEmail.sendEmail(emailAdress, emailSubject, emailText);
                if (!message.equals("")){
                    ra.addFlashAttribute("messageErreur", message);
                    return "redirect:/";
                }

            } catch (MessagingException | UnsupportedEncodingException e) {
                ra.addFlashAttribute("messageErreur", "Probl??me avec l'envoie de l'email de confirmation du compte.");
                return "redirect:/";
            }
            ra.addFlashAttribute("message", "Un email de confirmation de cr??ation du compte vous a ??t?? envoy??.");
            return "redirect:/";
        }

        if (createUserIn.getContact().equals("sms"))
        {
            try {
                String registerName = createUserIn.getLastname();
                String smsMessage = "Bonjour monsieur " + registerName + "," +
                        " Merci d'avoir cr??e un compte chez nous, vous pouvez maintenant effectuer vos r??servations plus facilement en vous connectant." +
                        " N'h??sitez pas ?? nous contacter si vous avez des questions.";
                SmsRequest smsRequest = new SmsRequest(createUserIn.getPhone(), smsMessage);
                ServiceSms.sendSms(smsRequest);
            } catch (Exception e) {
                ra.addFlashAttribute("messageErreur", "Compte cr???? mais probl??me avec l'envoi du sms de confirmation du compte.");
                return "redirect:/";
            }
            ra.addFlashAttribute("message", "Un SMS de confirmation de cr??ation du compte a ??t?? envoy??.");
            return "redirect:/";
        }

        ra.addFlashAttribute("message", "Votre compte a bien ??t?? cr????.");
        return "redirect:/";

    }

    @PostMapping("/newsLetter")
    public String newsLetter(String email, RedirectAttributes ra) throws MessagingException, UnsupportedEncodingException {
        String emailAdress = email;
        String emailSubject = "Merci pour votre abonnement ?? la Newsletter Resthony!";
        String emailText = "<p>Bonjour madame/monsieur,</p>"
                + "<p>Merci de l'int??r??t que vous nous portez.</p>"
                + "<p>Nous nous engageons ?? vous garder au courant de toute nouvelles fonctionnalit?? nous concernant. </p>"
                + "<p>A bient??t! </p>"
                + "<p>L'??quipe Resthony </p>";
        try {
            ServiceEmail.sendEmail(emailAdress, emailSubject, emailText);
        } catch (MessagingException | UnsupportedEncodingException e) {
            ra.addFlashAttribute("messageErreur", "Un probl??me est survenu, veuillez nous en excuser.");
            return "redirect:/";
        }
        ra.addFlashAttribute("message", "Merci beaucoup! Un email de confirmation vous a ??t?? envoy??.");
        return "redirect:/";
    }

}
