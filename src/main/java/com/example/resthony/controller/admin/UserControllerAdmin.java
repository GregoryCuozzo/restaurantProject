package com.example.resthony.controller.admin;

import com.example.resthony.constants.RoleEnum;
import com.example.resthony.model.dto.user.CreateUserIn;
import com.example.resthony.model.dto.user.PatchUserIn;
import com.example.resthony.model.dto.user.UserOut;
import com.example.resthony.repositories.UserRepository;
import com.example.resthony.services.principal.RestoService;
import com.example.resthony.services.principal.UserNotFoundException;
import com.example.resthony.services.principal.UserService;
import com.example.resthony.utils.BCryptManagerUtil;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * User controller
 */
@Controller
@Component
@RequestMapping("/admin/user")
public class UserControllerAdmin {

    private final UserService service;
    private final RestoService ServiceResto;

    public UserControllerAdmin(UserService service, RestoService serviceResto) {
        this.service = service;
        ServiceResto = serviceResto;
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
    public String createUser(@Valid @ModelAttribute("users") CreateUserIn createUserIn, BindingResult bindingResult, RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            ra.addFlashAttribute("warning", "Problème avec le register");
            System.out.println(bindingResult);
            return "/admin/users/create";
        }
        String restPasswordValue = BCryptManagerUtil.passwordEncoder().encode(createUserIn.getPassword());
        createUserIn.setPassword(restPasswordValue);
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

        service.patch(patchUserIn.getId(), patchUserIn);
        ra.addFlashAttribute("message", "L'utilisateur a été modifié");

        return "redirect:/admin/user/list";
    }


}
