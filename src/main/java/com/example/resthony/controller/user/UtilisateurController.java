package com.example.resthony.controller.user;

import com.example.resthony.model.dto.user.PatchUserIn;
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

    @PostMapping("/update")
    public String updateUser(@Valid @ModelAttribute("user") PatchUserIn patchUserIn, BindingResult bindingResult, RedirectAttributes ra) {
        if(bindingResult.hasErrors()) {
            return "redirect:/user/profil";
        }

        service.patch(patchUserIn.getId(), patchUserIn);
        ra.addFlashAttribute("message", "l'utilisateur a été modifié  ");

        return "redirect:/user/profil";
    }


    @GetMapping("/updatePass")
    public String updatePass (Model model){

        return "user/newpass.html";
    }

    @PostMapping("/updatePass")
    public String updatePass(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword, @RequestParam("newPassword2") String newPassword2, RedirectAttributes ra){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if(oldPassword.isEmpty() || newPassword.isEmpty() || newPassword2.isEmpty()){
            ra.addFlashAttribute("message", "Tous les champs doivent être remplis");
            return "redirect:/user/updatePass";
        }else if(!encoder.matches(oldPassword, service.getCurrentUser().getPassword()) ){
            ra.addFlashAttribute("message", "L'ancien password est mauvais");
            return "redirect:/user/updatePass";
        }else if(false){
            ra.addFlashAttribute("message", "Le mot de passe doit être de minimum 10 caratères et contenir au minimum des lettres, un chiffre et un caractère spécial");
            return "redirect:/user/updatePass";
        }else if(!newPassword.equals(newPassword2)){
            ra.addFlashAttribute("message", "Les mots de passe ne correspondent pas");
            return "redirect:/user/updatePass";
        }else{
            String passwordEncrypt = BCryptManagerUtil.passwordEncoder().encode(newPassword);
            service.updatePass(service.getCurrentUser().getId(),passwordEncrypt);
        }
        return "redirect:/user/profil";
    }



}

