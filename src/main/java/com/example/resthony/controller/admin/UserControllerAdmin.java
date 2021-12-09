package com.example.resthony.controller.admin;

import com.example.resthony.constants.RoleEnum;
import com.example.resthony.model.dto.user.CreateUserIn;
import com.example.resthony.model.dto.user.PatchUserIn;
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
@RequestMapping("/admin/user")
public class UserControllerAdmin {

    private final UserService service;
    private final RestoService ServiceResto;
    private final EmailService ServiceEmail;

    public UserControllerAdmin(UserService service, RestoService serviceResto, EmailService serviceEmail) {
        this.service = service;
        ServiceResto = serviceResto;
        ServiceEmail = serviceEmail;
    }
    @GetMapping("/list")
    public String all(Model model){
        model.addAttribute("Users",service.getAll());
        model.addAttribute("restaurants",ServiceResto.getAll());
        return "/admin/users/users.html";

    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("users", new CreateUserIn());
        model.addAttribute("restaurants",ServiceResto.getAll());
        model.addAttribute("rolesList", RoleEnum.values());
        return "/admin/users/create.html";
    }

    @PostMapping("/create")
    public String createUser(@Valid @ModelAttribute("users") CreateUserIn createUserIn, BindingResult bindingResult,RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return "/admin/users/create";
        }
        String message = service.checkDuplicateCreate(createUserIn);

        if (!message.equals("")) {
            ra.addFlashAttribute("messageErreur",message);
            return "redirect:/admin/user/create";
        }

        String restPasswordValue = BCryptManagerUtil.passwordEncoder().encode(createUserIn.getPassword());
        createUserIn.setPassword(restPasswordValue);
        createUserIn.roles = new ArrayList<>();

        createUserIn.addRole(RoleEnum.USER);
        try {
            //Info sur le user

            String registerName = createUserIn.getLastname();

            //Info email
            String emailAdress = createUserIn.getEmail();
            String emailSubject = "Compte crée chez Resthony.";
            String emailText = "<p>Bonjour monsieur "+registerName+",</p>"
                    + "<p>Un compte a été crée pour vous chez Resthony.</p>"
                    + "<p>N'hésitez pas à nous contacter si vous avez des questions.</p>";
            ServiceEmail.sendEmail(emailAdress, emailSubject, emailText);
        }
        catch (MessagingException | UnsupportedEncodingException e){
            ra.addFlashAttribute("messageErreur", "Compte crée mais problème avec l'envoie de l'email de confirmation du compte.");
            service.create(createUserIn);
            return "redirect:/admin/user/list";
        }
        ra.addFlashAttribute("message", "Utilisateur crée et email de confirmation de création de compte envoyé.");
        service.create(createUserIn);
        return "redirect:/admin/user/list";
    }



    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, RedirectAttributes ra) {
        try {
            service.delete(id);

        } catch (NotFoundException | UserNotFoundException e) {

        }
        ra.addFlashAttribute("message", "L'utilisateur  a été supprimé");
        return "redirect:/admin/user/list";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") String id, Model model) {
        model.addAttribute("users", service.get(Long.valueOf(id)));
        model.addAttribute("restaurants",ServiceResto.getAll());
        return "/admin/users/update.html";
    }

    @PostMapping("/update")
    public String updateUser(@Valid @ModelAttribute("users") PatchUserIn patchUserIn, BindingResult bindingResult, RedirectAttributes ra) {
        if(bindingResult.hasErrors()) {
            return "/admin/users/update.html";
        }

        String message = service.checkDuplicateUpdate(patchUserIn);

        if (!message.equals("")) {
            ra.addFlashAttribute("messageErreur",message);
            return "redirect:/admin/user/update/"+patchUserIn.getId();
        }

        service.patch(patchUserIn.getId(), patchUserIn);
        ra.addFlashAttribute("message", "L'utilisateur a été modifié.");

        return "redirect:/admin/user/list";
    }


    @GetMapping("/updatePass")
    public String updatePass (Model model){

        return "admin/newpass.html";
    }

    @PostMapping("/updatePass")
    public String updatePass(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword, @RequestParam("newPassword2") String newPassword2, RedirectAttributes ra){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Pattern p = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{10,}$");
        Matcher m = p.matcher(newPassword);
        boolean b = m.matches();
        if(oldPassword.isEmpty() || newPassword.isEmpty() || newPassword2.isEmpty()){
            ra.addFlashAttribute("messageErreur", "Tous les champs doivent être remplis");
            return "redirect:/admin/user/updatePass";
        }else if(!encoder.matches(oldPassword, service.getCurrentUser().getPassword()) ){
            ra.addFlashAttribute("messageErreur", "L'ancien mot de passe est mauvais");
            return "redirect:/admin/user/updatePass";
        }else if(!b){
            ra.addFlashAttribute("messageErreur", "Le mot de passe doit être de minimum 10 caratères et contenir au minimum des lettres, un chiffre et un caractère spécial");
            return "redirect:/admin/user/updatePass";
        }else if(!newPassword.equals(newPassword2)){
            ra.addFlashAttribute("messageErreur", "Les mots de passe ne correspondent pas");
            return "redirect:/admin/user/updatePass";
        }else if(oldPassword.equals(newPassword2)) {
            ra.addFlashAttribute("messageErreur", "Le nouveau mot de passe doit être différent de l'ancien");
            return "redirect:/admin/user/updatePass";
        }else{
            try {
                service.updatePass(service.getCurrentUser().getId(), newPassword);
            }
            catch(Exception exception){
                ra.addFlashAttribute("messageErreur", "Un erreur s'est produite, veuillez réassayer plus tard ou nous contacter si l'erreur persiste");
            }
        }
        ra.addFlashAttribute("message", "Votre mot de passe a bien été changé");
        return "redirect:/admin";
    }


}
