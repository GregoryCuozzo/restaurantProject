package com.example.resthony.controller.restaurants;
import com.example.resthony.entities.Restaurant;
import com.example.resthony.entities.User;
import com.example.resthony.services.impl.RestauDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

    @Controller
    @RequestMapping("/admin/restaurant")
    public class RestaurantsController {
        @Autowired
        private RestauDetailsServiceImpl service;


        @GetMapping
        public String restaurantPage(Model model) {
            List<Restaurant> listrestaurants = service.listAll();
            model.addAttribute("listRestaurant", listrestaurants);
            return "admin/restaurant";


        }


        @PostMapping("/save")
        public String saveRestaurant(Restaurant restaurant, RedirectAttributes ra) {
            service.save(restaurant);
            ra.addFlashAttribute("message", "vous avez bien enregistré un restaurant");
            return "/index";
        }


        @PostMapping("edit")
        public String editUser(Restaurant restaurant, RedirectAttributes ra) {
            service.save(restaurant);
            ra.addFlashAttribute("message", "La modification a été réalisée");
            return "redirect:/admin/users";

        }
    }
