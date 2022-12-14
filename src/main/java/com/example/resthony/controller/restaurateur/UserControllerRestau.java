package com.example.resthony.controller.restaurateur;

import com.example.resthony.constants.RoleEnum;
import com.example.resthony.model.dto.user.CreateUserIn;
import com.example.resthony.model.dto.user.PatchUserIn;
import com.example.resthony.model.entities.SmsRequest;
import com.example.resthony.services.principal.*;
import com.example.resthony.utils.BCryptManagerUtil;
import javassist.NotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User controller
 */
@Controller
@Component
@RequestMapping("/restaurateur/user")
public class UserControllerRestau {

    private final UserService service;
    private final RestoService ServiceResto;
    private final EmailService ServiceEmail;
    private final SmsService ServiceSms;

    public UserControllerRestau(UserService service, RestoService serviceResto, EmailService serviceEmail, SmsService serviceSms) {
        this.service = service;
        ServiceResto = serviceResto;
        ServiceEmail = serviceEmail;
        ServiceSms = serviceSms;
    }

    @GetMapping("/list")
    public String all(Model model) {
        int nbUsers = service.countUser();
        model.addAttribute("Users", service.getAll());
        model.addAttribute("restaurants", ServiceResto.getAll());
        model.addAttribute("nbUsers", nbUsers);
        return "/restaurateur/users/users.html";

    }

    @GetMapping("/create/{role}")
    public String create(@PathVariable("role") String role, Model model) {
        model.addAttribute("users", new CreateUserIn());
        model.addAttribute("restaurants", ServiceResto.getAll());
        model.addAttribute("role", role);
        return "/restaurateur/users/create.html";


    }

    @PostMapping("/create")
    public String createUser(@Valid @ModelAttribute("users") CreateUserIn createUserIn, BindingResult bindingResult, @RequestParam(name = "role") String role, Model model, RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("restaurants", ServiceResto.getAll());
            model.addAttribute("role", role);
            return "/restaurateur/users/create.html";
        }
        String message = service.checkDuplicateCreate(createUserIn);

        if (!message.equals("")) {
            ra.addFlashAttribute("messageErreur", message);
            return "redirect:/restaurateur/user/create/" + role;
        }

        String restPasswordValue = BCryptManagerUtil.passwordEncoder().encode(createUserIn.getPassword());
        createUserIn.setPassword(restPasswordValue);
        createUserIn.roles = new ArrayList<>();
        if (RoleEnum.ADMIN.name().equals(role)) {
            createUserIn.addRole(RoleEnum.ADMIN);
        } else if (RoleEnum.restaurateur.name().equals(role)) {
            createUserIn.addRole(RoleEnum.restaurateur);
        } else if (RoleEnum.USER.name().equals(role)) {
            createUserIn.addRole(RoleEnum.USER);
        } else {
            return "/restaurateur/users/create.html";
        }
        try {
            service.create(createUserIn);
        }
        catch(Exception e) {
            ra.addFlashAttribute("messageErreur", "Probl??me avec la cr??ation de l'utilisateur.");
            return "redirect:/restaurateur/user/list";
        }

        //Email de confirmation envoy?? ?? l'utilisateur
        if (createUserIn.getContact() != null) {
            if (createUserIn.getContact().equals("email")) {
                try {
                    //Info sur le user

                    String registerName = createUserIn.getLastname();

                    //Info email
                    String emailAdress = createUserIn.getEmail();
                    String emailSubject = "Compte cr??e chez Resthony.";
                    String emailText = "<p>Bonjour madame/monsieur " + registerName + ",</p>"
                            + "<p>Un compte a ??t?? cr??e pour vous chez Resthony.</p>"
                            + "<p>N'h??sitez pas ?? nous contacter si vous avez des questions.</p>";
                    ServiceEmail.sendEmail(emailAdress, emailSubject, emailText);
                } catch (MessagingException | UnsupportedEncodingException e) {
                    ra.addFlashAttribute("messageErreur", "Compte cr??e mais probl??me avec l'envoie de l'email de confirmation de cr??ation du compte.");
                    return "redirect:/restaurateur/user/list";
                }
                ra.addFlashAttribute("message", "Utilisateur cr??e et email de confirmation de cr??ation de compte envoy??.");
                return "redirect:/restaurateur/user/list";
            }

            if (createUserIn.getContact().equals("sms")) {
                try {
                    String registerName = createUserIn.getLastname();
                    String smsMessage = "Bonjour madame/monsieur " + registerName + "," +
                            " Un compte a ??t?? cr??e pour vous chez Resthony." +
                            " N'h??sitez pas ?? nous contacter si vous avez des questions.";
                    SmsRequest smsRequest = new SmsRequest(createUserIn.getPhone(), smsMessage);
                    ServiceSms.sendSms(smsRequest);
                } catch (Exception e) {
                    ra.addFlashAttribute("messageErreur", "Compte cr???? mais probl??me avec l'envoi du sms de confirmation de cr??ation du compte.");
                    return "redirect:/restaurateur/user/list";
                }
                ra.addFlashAttribute("message", "Un SMS de confirmation de cr??ation du compte a ??t?? envoy??.");
                return "redirect:/restaurateur/user/list";
            }
        }
        ra.addFlashAttribute("message", "Compte cr????");
        return "redirect:/restaurateur/user/list";
    }


    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, RedirectAttributes ra) {
        try {
            service.delete(id);

        } catch (NotFoundException | UserNotFoundException e) {
            ra.addFlashAttribute("messageErreur", "Pas d'utilisateur trouv?? ?? l'id" + id);
            return "redirect:/restaurateur/user/list";
        }
        ra.addFlashAttribute("message", "L'utilisateur  a ??t?? supprim??");
        return "redirect:/restaurateur/user/list";
    }

    @GetMapping("/update/{id}/{role}")
    public String update(@PathVariable("id") String id, @PathVariable("role") String role, Model model) {
        model.addAttribute("users", service.get(Long.valueOf(id)));
        model.addAttribute("restaurants", ServiceResto.getAll());
        model.addAttribute("role", role);
        return "/restaurateur/users/update.html";
    }

    @PostMapping("/update")
    public String updateUser(@Valid @ModelAttribute("users") PatchUserIn patchUserIn, BindingResult bindingResult, @RequestParam(name = "role") String role, Model model, RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("restaurants", ServiceResto.getAll());
            model.addAttribute("role", role);
            return "/restaurateur/users/update.html";
        }

        String message = service.checkDuplicateUpdate(patchUserIn);

        if (!message.equals("")) {
            model.addAttribute("restaurants", ServiceResto.getAll());
            model.addAttribute("role", role);
            ra.addFlashAttribute("messageErreur", message);
            return "redirect:/restaurateur/user/update/" + patchUserIn.getId() + "/" + role;
        }


        try {
            service.patch(patchUserIn.getId(), patchUserIn);
        }
        catch(Exception e){
            ra.addFlashAttribute("messageErreur", "Probl??me avec la modification de l'utilisateur.");
            return "redirect:/restaurateur/user/list";
        }

        ra.addFlashAttribute("message", "L'utilisateur a ??t?? modifi??");

        return "redirect:/restaurateur/user/list";
    }


    @GetMapping("/updatePass")
    public String updatePass(Model model) {

        return "restaurateur/newpass.html";
    }

    @PostMapping("/updatePass")
    public String updatePass(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword, @RequestParam("newPassword2") String newPassword2, RedirectAttributes ra) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Pattern p = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{10,}$");
        Matcher m = p.matcher(newPassword);
        boolean b = m.matches();
        if (oldPassword.isEmpty() || newPassword.isEmpty() || newPassword2.isEmpty()) {
            ra.addFlashAttribute("messageErreur", "Tous les champs doivent ??tre remplis");
            return "redirect:/restaurateur/user/updatePass";
        } else if (!encoder.matches(oldPassword, service.getCurrentUser().getPassword())) {
            ra.addFlashAttribute("messageErreur", "L'ancien mot de passe est mauvais");
            return "redirect:/restaurateur/user/updatePass";
        } else if (!b) {
            ra.addFlashAttribute("messageErreur", "Le mot de passe doit ??tre de minimum 10 carat??res et contenir au minimum des lettres, un chiffre et un caract??re sp??cial");
            return "redirect:/restaurateur/user/updatePass";
        } else if (!newPassword.equals(newPassword2)) {
            ra.addFlashAttribute("messageErreur", "Les mots de passe ne correspondent pas");
            return "redirect:/restaurateur/user/updatePass";
        } else if (oldPassword.equals(newPassword2)) {
            ra.addFlashAttribute("messageErreur", "Le nouveau mot de passe doit ??tre diff??rent de l'ancien");
            return "redirect:/restaurateur/user/updatePass";
        } else {
            try {
                service.updatePass(service.getCurrentUser().getId(), newPassword);
            } catch (Exception exception) {
                ra.addFlashAttribute("messageErreur", "Un erreur s'est produite, veuillez r??assayer plus tard ou nous contacter si l'erreur persiste");
            }
        }
        ra.addFlashAttribute("message", "Votre mot de passe a bien ??t?? chang??");
        return "redirect:/restaurateur";
    }


}