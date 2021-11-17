package com.example.resthony.controller.restaurants;
import com.example.resthony.controller.restaurants.dto.CreateRestoIn;
import com.example.resthony.controller.restaurants.dto.PatchRestoIn;
import com.example.resthony.entities.Restaurant;
import com.example.resthony.entities.User;
import com.example.resthony.services.impl.RestauDetailsServiceImpl;
import com.example.resthony.services.impl.RestoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

    @Controller
    @RequestMapping("/admin/restaurant")
    public class RestaurantsController {
        private final RestoService restoService;

//        @Autowired
//        private RestauDetailsServiceImpl service;

        @Autowired
        public RestaurantsController(RestoService restoService) {
            this.restoService = restoService;
        }

        @GetMapping("/list")
        public String all(Model model){
            model.addAttribute("restos",restoService.getAll());
            return "restaurants.html";

        }

        @GetMapping("/create")
        public String create(Model model) {
            model.addAttribute("resto", new CreateRestoIn());
            return "create.html";
        }

        @PostMapping("/create")
        public String createResto(@Valid @ModelAttribute("resto") CreateRestoIn createRestoIn, BindingResult bindingResult) {
            if(bindingResult.hasErrors()) {
                return "/create";
            }

            restoService.create(createRestoIn);

            return "redirect:/web/resto/list";
        }

        @GetMapping("/update/{id}")
        public String update(@PathVariable("id") String id, Model model) {
            model.addAttribute("resto", restoService.get(Integer.valueOf(id)));
            return "update.html";
        }

        @PostMapping("/update")
        public String updateResto(@Valid @ModelAttribute("resto") PatchRestoIn patchRestoIn, BindingResult bindingResult) {
            if(bindingResult.hasErrors()) {
                return "/update";
            }

            restoService.patch(patchRestoIn.getId(), patchRestoIn);

            return "redirect:/web/resto/list";
        }


    }
