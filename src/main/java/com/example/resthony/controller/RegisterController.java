package com.example.resthony.controller;

import com.example.resthony.model.dto.user.CreateUserIn;
import com.example.resthony.model.dto.user.UserOut;
import com.example.resthony.services.principal.UserService;
import com.example.resthony.utils.BCryptManagerUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/register")
public class RegisterController {

    private final UserService service;
    private AuthenticationManager authManager;

    public RegisterController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public String registerPage() {
        return "/public/register";
    }

    @PostMapping("/create")
    public String createUser(@Valid @ModelAttribute("users") CreateUserIn createUserIn, BindingResult bindingResult, RedirectAttributes ra, HttpServletRequest response) throws ServletException {
        if (bindingResult.hasErrors()) {
            ra.addFlashAttribute("warning", "Problème avec le register");
            return "/public/register.html";
        }

           /* if(oldPassword.isEmpty() || newPassword.isEmpty() || newPassword2.isEmpty()){
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
        }*/


        // String uncryptedPass = createUserIn.getPassword();
        String restPasswordValue = BCryptManagerUtil.passwordEncoder().encode(createUserIn.getPassword());
        createUserIn.setPassword(restPasswordValue);
        service.register(createUserIn);
        // response.login(createUserIn.getUsername(), uncryptedPass);
        return "redirect:/user/index.html";
    }
/*
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValisationExceptions(
            MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
*/



}
