package com.example.resthony.controller.admin;

import com.example.resthony.entities.User;
import com.example.resthony.services.impl.UserNotFoundException;
import com.example.resthony.services.impl.UsersDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Admin controller
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UsersDetailsServiceImpl service;

    @GetMapping
    public String adminPage(Model model) {

        return "admin/index";
    }


    @GetMapping("/users")
    public String showUserList(Model model) {

        List<User> listUsers = service.listAll();
        model.addAttribute("listUsers", listUsers);
        return "admin/users";

    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
        try {
            User user = service.get(id);
            model.addAttribute("user", user);
            model.addAttribute("pageTitle", " mise à jour des informations (ID:" + id + ")");

            return "/editUser";

        } catch (UserNotFoundException e) {


        }
        ra.addFlashAttribute("message", "les informations ont bien été modifiées");
        return "redirect:/admin/users";

    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, RedirectAttributes ra) {
        try {
            service.delete(id);

        } catch (UserNotFoundException e) {

        }
        ra.addFlashAttribute("message", "l'utilisateur a été supprimé ");
        return "redirect:/admin/users";
    }

    @PostMapping("/save")
    public String saveUser(User user, RedirectAttributes ra) {
        service.save(user);
        ra.addFlashAttribute("message", "vous vous êtes bien enregistré");
        return "redirect:/admin/users";
    }

}

