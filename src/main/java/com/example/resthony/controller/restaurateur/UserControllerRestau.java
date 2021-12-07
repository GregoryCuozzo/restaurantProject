package com.example.resthony.controller.restaurateur;

import com.example.resthony.constants.RoleEnum;
import com.example.resthony.model.dto.user.CreateUserIn;
import com.example.resthony.model.dto.user.PatchUserIn;
import com.example.resthony.model.entities.User;
import com.example.resthony.services.principal.RestoService;
import com.example.resthony.services.principal.UserNotFoundException;
import com.example.resthony.services.principal.UserService;
import com.example.resthony.utils.BCryptManagerUtil;
import javassist.NotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.json.bind.annotation.JsonbTransient;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * User controller
 */
@Controller
@Component
@RequestMapping("/restaurateur/user")
public class UserControllerRestau {

    private final UserService service;
    private final RestoService ServiceResto;

    public UserControllerRestau(UserService service, RestoService serviceResto) {
        this.service = service;
        ServiceResto = serviceResto;
    }
    @GetMapping("/list")
    public String all(Model model){
        model.addAttribute("Users",service.getAll());
        model.addAttribute("restaurants",ServiceResto.getAll());

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