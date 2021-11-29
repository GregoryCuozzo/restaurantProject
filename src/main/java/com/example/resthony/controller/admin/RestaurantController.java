package com.example.resthony.controller.admin;

import com.example.resthony.model.dto.restaurant.CreateRestoIn;
import com.example.resthony.model.dto.restaurant.PatchRestoIn;
import com.example.resthony.services.principal.RestoService;
import com.example.resthony.services.principal.VilleService;
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
    public class RestaurantController {
        private final RestoService restoService;
        private final VilleService villeService;


        @Autowired
        public RestaurantController(RestoService restoService, VilleService villeService) {
            this.restoService = restoService;
            this.villeService = villeService;
        }

        @GetMapping("/list")
        public String all(Model model){
            model.addAttribute("restos",restoService.getAll());
            model.addAttribute("villes",villeService.getAll());
            return "/admin/restaurant/restaurants.html";

        }

        @GetMapping("/create")
        public String create(Model model) {
            model.addAttribute("resto", new CreateRestoIn());
            model.addAttribute("villes",villeService.getAll());
            return "/admin/restaurant/create.html";
        }

        @PostMapping("/create")
        public String createResto(@Valid @ModelAttribute("resto") CreateRestoIn createRestoIn, BindingResult bindingResult, Model model) {
            if(bindingResult.hasErrors()) {
                return "/admin/restaurant/create.html";
            }

            restoService.create(createRestoIn);
            model.addAttribute("restos",restoService.getAll());
            return "/admin/restaurant/restaurants.html";
        }

    @GetMapping("/delete/{id}")
    public String deleteResto(@PathVariable("id") Long id, RedirectAttributes ra) {
        try {
            restoService.delete(id);

        } catch (NotFoundException e) {

        }
        ra.addFlashAttribute("message", "le restaurant a été supprimé ");
        return "redirect:/admin/restaurant/list";
    }

        @GetMapping("/update/{id}")
        public String update(@PathVariable("id") String id, Model model) {
            model.addAttribute("resto", restoService.get(Long.valueOf(id)));
            model.addAttribute("villes",villeService.getAll());
            return "/admin/restaurant/update.html";
        }

        @PostMapping("/update")
        public String updateResto(@Valid @ModelAttribute("resto") PatchRestoIn patchRestoIn, BindingResult bindingResult, RedirectAttributes ra) {
            if(bindingResult.hasErrors()) {
                return "/admin/restaurant/update";
            }

            restoService.patch(patchRestoIn.getId(), patchRestoIn);
            ra.addFlashAttribute("message", "le restaurant a été modifié  ");

            return "redirect:/admin/restaurant/list";
        }


    }
