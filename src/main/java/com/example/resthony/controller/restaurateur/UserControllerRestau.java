package com.example.resthony.controller.restaurateur;

import com.example.resthony.constants.RoleEnum;
import com.example.resthony.model.dto.user.CreateUserIn;
import com.example.resthony.model.dto.user.PatchUserIn;
import com.example.resthony.services.principal.EmailService;
import com.example.resthony.services.principal.RestoService;
import com.example.resthony.services.principal.UserNotFoundException;
import com.example.resthony.services.principal.UserService;
import com.example.resthony.utils.BCryptManagerUtil;
import javassist.NotFoundException;
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

    public UserControllerRestau(UserService service, RestoService serviceResto, EmailService serviceEmail) {
        this.service = service;
        ServiceResto = serviceResto;
        ServiceEmail = serviceEmail;
    }
    @GetMapping("/list")
    public String all(Model model){
        int nbUsers = service.countUser();
        System.out.println(nbUsers);
        model.addAttribute("Users",service.getAll());
        model.addAttribute("restaurants",ServiceResto.getAll());
        model.addAttribute("nbUsers", nbUsers);
        return "/restaurateur/users/users.html";

    }

    @GetMapping("/create/{role}")
    public String create(@PathVariable("role") String role, Model model) {
        model.addAttribute("users", new CreateUserIn());
        model.addAttribute("restaurants",ServiceResto.getAll());
        model.addAttribute("role", role);
        return "/restaurateur/users/create.html";


    }

    @PostMapping("/create")
    public String createUser(@Valid @ModelAttribute("users") CreateUserIn createUserIn, BindingResult bindingResult, @RequestParam(name = "role") String role,Model model,RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("restaurants",ServiceResto.getAll());
            model.addAttribute("role", role);
            return "/restaurateur/users/create.html";
        }
        String message = service.checkDuplicateCreate(createUserIn);

        if (!message.equals("")) {
            ra.addFlashAttribute("messageErreur",message);
            return "redirect:/restaurateur/user/create/"+role;
        }

        String restPasswordValue = BCryptManagerUtil.passwordEncoder().encode(createUserIn.getPassword());
        createUserIn.setPassword(restPasswordValue);
        createUserIn.roles = new ArrayList<>();
        if(RoleEnum.ADMIN.name().equals(role)){
            createUserIn.addRole(RoleEnum.ADMIN);
        }else if(RoleEnum.restaurateur.name().equals(role)){
            createUserIn.addRole(RoleEnum.restaurateur);
        }else if(RoleEnum.USER.name().equals(role)){
            createUserIn.addRole(RoleEnum.USER);
        }else{
            return "/restaurateur/users/create.html";
        }
            //Email de confirmation envoyé à l'utilisateur
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
            return "redirect:/restaurateur/user/list";
        }
        ra.addFlashAttribute("message", "Utilisateur crée et email de confirmation de création de compte envoyé.");
        service.create(createUserIn);
        return "redirect:/restaurateur/user/list";
    }





    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, RedirectAttributes ra) {
        try {
            service.delete(id);

        } catch (NotFoundException | UserNotFoundException e) {

        }
        ra.addFlashAttribute("message", "L'utilisateur  a été supprimé");
        return "redirect:/restaurateur/user/list";
    }

    @GetMapping("/update/{id}/{role}")
    public String update(@PathVariable("id") String id,@PathVariable("role") String role, Model model) {
        model.addAttribute("users", service.get(Long.valueOf(id)));
        model.addAttribute("restaurants",ServiceResto.getAll());
        model.addAttribute("role", role);
        return "/restaurateur/users/update.html";
    }

    @PostMapping("/update")
    public String updateUser(@Valid @ModelAttribute("users") PatchUserIn patchUserIn, BindingResult bindingResult,@RequestParam(name = "role") String role, Model model, RedirectAttributes ra) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("restaurants",ServiceResto.getAll());
            model.addAttribute("role", role);
            System.out.println(patchUserIn.getRoles());
            return "/restaurateur/users/update.html";
        }

        System.out.println(patchUserIn);
        String message = service.checkDuplicateUpdate(patchUserIn);

        if (!message.equals("")) {
            model.addAttribute("restaurants",ServiceResto.getAll());
            model.addAttribute("role", role);
            ra.addFlashAttribute("messageErreur",message);
            return "redirect:/restaurateur/user/update/"+patchUserIn.getId()+"/"+role;
        }

        service.patch(patchUserIn.getId(), patchUserIn);
        ra.addFlashAttribute("message", "L'utilisateur a été modifié");

        return "redirect:/restaurateur/user/list";
    }


}