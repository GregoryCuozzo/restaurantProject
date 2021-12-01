package com.example.resthony.controller;

import com.example.resthony.model.dto.user.CreateUserIn;
import com.example.resthony.model.dto.user.UserOut;
import com.example.resthony.services.principal.UserService;
import com.example.resthony.utils.BCryptManagerUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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
            return "/public/login";
        }
        // String uncryptedPass = createUserIn.getPassword();
        String restPasswordValue = BCryptManagerUtil.passwordEncoder().encode(createUserIn.getPassword());
        createUserIn.setPassword(restPasswordValue);
        service.register(createUserIn);
        // response.login(createUserIn.getUsername(), uncryptedPass);
        return "redirect:/user/index.html";
    }

}
