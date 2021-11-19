package com.example.resthony.controller.restaurant;
import com.example.resthony.model.dto.restaurant.CreateRestoIn;
import com.example.resthony.model.dto.restaurant.PatchRestoIn;
import com.example.resthony.services.principal.RestoService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
    @RequestMapping("/admin/restaurant")
    public class RestaurantsController {
        private final RestoService restoService;



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

    @GetMapping("/delete/{id}")
    public String deleteResto(@PathVariable("id") Long id, RedirectAttributes ra) {
        try {
            restoService.delete(id);

        } catch (NotFoundException e) {

        }
        ra.addFlashAttribute("message", "l'utilisateur a été supprimé ");
        return "redirect:/admin/restaurant/list";
    }

        @GetMapping("/update/{id}")
        public String update(@PathVariable("id") String id, Model model) {
            model.addAttribute("resto", restoService.get(Long.valueOf(id)));
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
