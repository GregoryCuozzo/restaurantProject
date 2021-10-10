package com.example.resthony.controller.user;

import com.example.resthony.entities.User;
import com.example.resthony.services.impl.UserNotFoundException;
import com.example.resthony.services.impl.UsersDetailsServiceImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

/**
 * User controller
 */
@Controller
@Component

public class UserController {

    public UsersDetailsServiceImpl service ;

    @GetMapping("/user")
    public String userPage() {
        return "user/index";
    }

    @GetMapping("/user/register")
    public String formPage(){
        return "form";
    }

    @GetMapping("/users")
    public String showUserList(Model model) {

        List<User> listUsers = service.listAll();
        model.addAttribute("listUsers", listUsers);
        return "users";

    }

    // ------------------------ getting the registration form -------------------------------
    @GetMapping("/users/new")
    public String ShowNewForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("pagetitle","Ajout d'un nouvel utilisateur");
        return "form";
    }

    // ------------------------ save user database ------------------------------------------

    @PostMapping("/users/save")
    public String saveUser(User user, RedirectAttributes ra) {
        service.save(user);
        ra.addFlashAttribute("message", "vous vous êtes bien enregistré");
        return "redirect:/users";
    }

    // ------------------------- get the edit form ------------------------------------------

    @GetMapping("/users/edit/{id}")
    public String showEditForm(@PathVariable("id") int id, Model model, RedirectAttributes ra) {
        try {
            User user = service.get(id);
            model.addAttribute("user", user);
            model.addAttribute("pageTitle"," mise à jour des informations (ID:" +id +")");

            return "form";

        } catch (UserNotFoundException e) {

            ra.addFlashAttribute("message","les informations ont bien été modifiées");
            return "redirect:/users";

        }
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable("id") int id, RedirectAttributes ra){
        try{
            service.delete(id);

        }
        catch (UserNotFoundException e){


        }
        ra.addFlashAttribute("message","l'utilisateur a été supprimé ");
        return "redirect:/users";
    }
}
