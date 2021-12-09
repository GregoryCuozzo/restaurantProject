package com.example.resthony.controller.user;

import com.example.resthony.model.dto.user.PatchUserIn;
import com.example.resthony.model.dto.user.UserOut;
import com.example.resthony.repositories.UserRepository;
import com.example.resthony.services.details.UsersDetailsServiceImpl;
import com.example.resthony.services.principal.UserService;
import com.example.resthony.utils.BCryptManagerUtil;
import org.hibernate.annotations.Cascade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Admin controller
 */
@Controller
@RequestMapping("/user")
public class UtilisateurController {

    private final UserService service;

    public UtilisateurController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public String userPage(Model model) {

        return "/user/index.html";

    }


    @GetMapping("/profil")
    //
    public String getUser(Model model){
        model.addAttribute("user", service.getCurrentUser());
        return "/user/profil.html";

    }

    @GetMapping("/update")
    public String update(Model model) {
        model.addAttribute("user", service.getCurrentUser());
        return "user/profil.html";
    }

    @PostMapping("/update")
    public String updateUser(@Valid @ModelAttribute("user") PatchUserIn patchUserIn, BindingResult bindingResult, RedirectAttributes ra) {
        if(bindingResult.hasErrors()) {
            return "/user/profil.html";
        }

        // Check duplicate
        String message = service.checkDuplicateUpdate(patchUserIn);

        if (!message.equals("")) {
            ra.addFlashAttribute("messageErreur",message);
            return "redirect:/user/update";
        }
        service.patch(patchUserIn.getId(), patchUserIn);
        ra.addFlashAttribute("message", "Votre profil a bien été modifié");

        return "redirect:/user/profil";
    }


    @GetMapping("/updatePass")
    public String updatePass (Model model){

        return "user/newpass.html";
    }

    @PostMapping("/updatePass")
    public String updatePass(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword, @RequestParam("newPassword2") String newPassword2, RedirectAttributes ra){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Pattern p = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{10,}$");
        Matcher m = p.matcher(newPassword);
        boolean b = m.matches();
        if(oldPassword.isEmpty() || newPassword.isEmpty() || newPassword2.isEmpty()){
            ra.addFlashAttribute("messageErreur", "Tous les champs doivent être remplis");
            return "redirect:/user/updatePass";
        }else if(!encoder.matches(oldPassword, service.getCurrentUser().getPassword()) ){
            ra.addFlashAttribute("messageErreur", "L'ancien mot de passe est mauvais");
            return "redirect:/user/updatePass";
        }else if(!b){
            ra.addFlashAttribute("messageErreur", "Le mot de passe doit être de minimum 10 caratères et contenir au minimum des lettres, un chiffre et un caractère spécial");
            return "redirect:/user/updatePass";
        }else if(!newPassword.equals(newPassword2)){
            ra.addFlashAttribute("messageErreur", "Les mots de passe ne correspondent pas");
            return "redirect:/user/updatePass";
        }else if(oldPassword.equals(newPassword2)) {
            ra.addFlashAttribute("messageErreur", "Le nouveau mot de passe doit être différent de l'ancien");
            return "redirect:/user/updatePass";
        }else{
            try {
                service.updatePass(service.getCurrentUser().getId(), newPassword);
            }
            catch(Exception exception){
                ra.addFlashAttribute("messageErreur", "Un erreur s'est produite, veuillez réassayer plus tard ou nous contacter si l'erreur persiste");
            }
        }
        ra.addFlashAttribute("message", "Votre mot de passe a bien été changé");
        return "redirect:/user/profil";
    }



}

