package com.example.resthony.controller.restaurateur;

import com.example.resthony.model.dto.restaurant.CreateRestoIn;
import com.example.resthony.model.dto.restaurant.PatchRestoIn;
import com.example.resthony.services.principal.RestoService;
import com.example.resthony.services.principal.UserService;
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
    @RequestMapping("/restaurateur/restaurant")
    public class RestaurantController {
        private final RestoService restoService;
        private final VilleService villeService;



        @Autowired
        public RestaurantController(RestoService restoService, VilleService villeService) {
            this.restoService = restoService;
            this.villeService = villeService;
        }

        @GetMapping("/list")
        public String all(Model model, RedirectAttributes ra){
            if(villeService.getAll().isEmpty()){
                ra.addFlashAttribute("messageErreur","Veuillez d'abord définir la ville dans laquelle votre restaurant se trouve");
                return "redirect:/restaurateur/villes/list";
            }

            model.addAttribute("restos",restoService.getAll());
            model.addAttribute("villes",villeService.getAll());
            return "/restaurateur/restaurant/restaurants.html";

        }

        @GetMapping("/create")
        public String create(Model model) {
            model.addAttribute("resto", new CreateRestoIn());
            model.addAttribute("villes",villeService.getAll());
            return "/restaurateur/restaurant/create.html";
        }

        @PostMapping("/create")
        public String createResto(@Valid @ModelAttribute("resto") CreateRestoIn createRestoIn, BindingResult bindingResult, Model model, RedirectAttributes ra) {
            if(bindingResult.hasErrors()) {
                model.addAttribute("villes",villeService.getAll());
                return "/restaurateur/restaurant/create.html";
            }


            try{
                restoService.create(createRestoIn);
            }
            catch (Exception e){
                ra.addFlashAttribute("messageErreur", "Problème avec la création du restaurant");
                return "redirect:/restaurateur/restaurant/list";
            }
            model.addAttribute("restos",restoService.getAll());
            return "redirect:/restaurateur/restaurant/list";
        }

    @GetMapping("/delete/{id}")
    public String deleteResto(@PathVariable("id") Long id, RedirectAttributes ra) {
        try {
            restoService.delete(id);


        } catch (NotFoundException e) {
            ra.addFlashAttribute("messageErreur", "Pas d'utilisateur touvé avec l'id " + id);
            return "redirect:/restaurateur/restaurant/list";
        }
        ra.addFlashAttribute("message", "le restaurant a été supprimé ");
        return "redirect:/restaurateur/restaurant/list";
    }

        @GetMapping("/update/{id}")
        public String update(@PathVariable("id") String id, Model model) {
            model.addAttribute("resto", restoService.get(Long.valueOf(id)));
            model.addAttribute("villes",villeService.getAll());
            return "/restaurateur/restaurant/update.html";
        }

        @PostMapping("/update")
        public String updateResto(@Valid @ModelAttribute("resto") PatchRestoIn patchRestoIn, BindingResult bindingResult,Model model, RedirectAttributes ra) {
            if(bindingResult.hasErrors()) {
                model.addAttribute("villes",villeService.getAll());
                return "/restaurateur/restaurant/update.html";
            }

            try{
                restoService.patch(patchRestoIn.getId(), patchRestoIn);
            }
            catch (Exception e){
                ra.addFlashAttribute("messageErreur", "Problème dans la modification du restaurant.");
                return "redirect:/restaurateur/restaurant/list";
            }


            ra.addFlashAttribute("message", "le restaurant a été modifié  ");

            return "redirect:/restaurateur/restaurant/list";
        }


    }
