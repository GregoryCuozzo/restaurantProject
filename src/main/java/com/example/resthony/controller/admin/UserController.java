package com.example.resthony.controller.admin;

import com.example.resthony.model.dto.user.CreateUserIn;
import com.example.resthony.model.dto.user.PatchUserIn;
import com.example.resthony.services.principal.RestoService;
import com.example.resthony.services.principal.UserNotFoundException;
import com.example.resthony.services.principal.UserService;
import javassist.NotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

/**
 * User controller
 */
@Controller
@Component
@RequestMapping("/admin/user")
public class UserController {

    private final UserService service;
    private final RestoService ServiceResto;

    public UserController(UserService service, RestoService serviceResto) {
        this.service = service;
        ServiceResto = serviceResto;
    }
    @GetMapping("/list")
    public String all(Model model){
        model.addAttribute("Users",service.getAll());
        model.addAttribute("restaurants",ServiceResto.getAll());
        return "user/users.html";

    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("users", new CreateUserIn());
        return "user/create.html";
    }

    @PostMapping("/create")
    public String createResto(@Valid @ModelAttribute("users") CreateUserIn createUserIn, BindingResult bindingResult,RedirectAttributes ra ) {
        if(bindingResult.hasErrors()) {
            return "/create";
        }

        service.create(createUserIn);
        ra.addFlashAttribute("message", "l'utilisateur  a été rajouté ");

        return "redirect:/admin/user/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, RedirectAttributes ra) {
        try {
            service.delete(id);

        } catch (NotFoundException | UserNotFoundException e) {

        }
        ra.addFlashAttribute("message", "l'utilisateur  a été supprimé ");
        return "redirect:/admin/user/list";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") String id, Model model) {
        model.addAttribute("users", service.get(Long.valueOf(id)));
        model.addAttribute("restaurants",ServiceResto.getAll());
        return "user/update.html";
    }

    @PostMapping("/update")
    public String updateResto(@Valid @ModelAttribute("users") PatchUserIn patchUserIn, BindingResult bindingResult, RedirectAttributes ra) {
        if(bindingResult.hasErrors()) {
            return "/update";
        }

        service.patch(patchUserIn.getId(), patchUserIn);
        ra.addFlashAttribute("message", "l'utilisateur a été modifié  ");

        return "redirect:/admin/user/list";
    }


}
