package com.example.resthony.controller;

import com.example.resthony.services.principal.UserNotFoundException;
import com.example.resthony.services.principal.UserService;
import com.example.resthony.utils.Utility;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@Controller
@RequestMapping("forgotPassword")
public class ForgotPasswordController {

    @Autowired
    private UserService userService;

    @Autowired
    private JavaMailSender mailSender;

    @GetMapping("/reset")
    public String showForgotPasswordForm(Model model) {
        return "/public/forgotPasswordForm.html";
    }

    @PostMapping("/reset")
    public String processForgotPasswordForm(HttpServletRequest request, RedirectAttributes ra) throws UserNotFoundException, MailSendException, MessagingException, UnsupportedEncodingException {
        if(request.getParameter("email").isEmpty()) {
            ra.addFlashAttribute("messageErreur","Veuillez remplir tous les champs");
            return "redirect:/forgotPassword/reset";
        }

        String email = request.getParameter("email");
        String token = RandomString.make(45);

        try {

            //set Token
            userService.updateResetPassword(token, email);

            //Gérérer lien reset password
            String resetPasswordLink = Utility.getSiteURL(request) + "/reset_password?token=" +token ;

            sendEmail(email, resetPasswordLink);

            //Envoyer email
            ra.addFlashAttribute("message", "Un message a été envoyé à l'adresse email : " +email);

        } catch (UserNotFoundException e) {
           ra.addFlashAttribute("messageErreur", e.getMessage());
        } catch(MessagingException | UnsupportedEncodingException | MailSendException e){
            ra.addFlashAttribute("messageErreur", "Erreur pendant l'envoi de l'email. Veuillez réessayer plus tard.");
        }

        return "redirect:/forgotPassword/reset";
    }

    private void sendEmail(String email, String resetPasswordLink) throws MessagingException, UnsupportedEncodingException, MailSendException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("contact@resthony.com", "Resthony support");
        helper.setTo(email);
        String subject = "Lien pour reset votre mot de passe";
        String content = "<p>Bonjour,</p>"
                + "<p>Vous avez demandé un reset de votre mot de passe.</p>"
                + "<p>Cliquez sur le lien ci-dessous pour changer votre mot de passe :</p>"
                +"<p><b><a href=\""+resetPasswordLink+"\" > Changer mon mot de passe</a></b></p>"
                +"<p>Ignorez cet email si vous vous souvenez du mot de passe ou si vous n'avez fais demandé ce changement.</p>";
        helper.setSubject(subject);
        helper.setText(content, true);

        mailSender.send(message);

    }

}
