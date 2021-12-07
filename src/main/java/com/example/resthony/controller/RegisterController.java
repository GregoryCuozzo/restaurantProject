package com.example.resthony.controller;

import com.example.resthony.model.dto.user.CreateUserIn;
import com.example.resthony.model.dto.user.UserOut;
import com.example.resthony.model.entities.User;
import com.example.resthony.services.principal.UserService;
import com.example.resthony.utils.BCryptManagerUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
            ra.addFlashAttribute("messageErreur",message);
            return "redirect:/register";
        }

        // ---------------- AUTO LOGIN APRES REGISTER A FAIRE --------------------------------

        // String uncryptedPass = createUserIn.getPassword();
        String restPasswordValue = BCryptManagerUtil.passwordEncoder().encode(createUserIn.getPassword());
        createUserIn.setPassword(restPasswordValue);
        service.register(createUserIn);
        // response.login(createUserIn.getUsername(), uncryptedPass);
        return "redirect:/user/index.html";

    }


}
