package com.example.resthony.controller.restaurateur;

import com.example.resthony.model.dto.horaire.CreateHoraireIn;
import com.example.resthony.model.dto.horaire.PatchHoraireIn;
import com.example.resthony.model.dto.restaurant.PatchRestoIn;
import com.example.resthony.services.principal.HoraireService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;


@Controller
@RequestMapping("/restaurateur/horaire")
public class HoraireController {
    private final HoraireService horaireService;

    @Autowired
    public HoraireController(HoraireService horaireService) {
        this.horaireService = horaireService;
    }

    @GetMapping("/list/{id}")
    public String all(@PathVariable("id") Long id, Model model) {
        model.addAttribute("resto", id);
        model.addAttribute("horaires", horaireService.findByRestaurant(id));
        return "/restaurateur/horaire/horaires.html";
    }

    @GetMapping("/create/{id}")
    public String create(@PathVariable("id") Long id, Model model) {
        model.addAttribute("resto", id);
        System.out.println(id);
        model.addAttribute("horaire", new CreateHoraireIn());
        return "/restaurateur/horaire/create.html";
    }

    @PostMapping("/create")
    public String createHoraire(@Valid @ModelAttribute("horaire") CreateHoraireIn createHoraireIn, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "/restaurateur/horaire/create.html";
        }

        horaireService.create(createHoraireIn);
        model.addAttribute("horaires", horaireService.findByRestaurant(createHoraireIn.restaurant));
        return "redirect:/restaurateur/horaire/list/" + createHoraireIn.restaurant;
    }


    @GetMapping("/delete/{id}")
    public String deleteHoraire(@PathVariable("id") Long id, RedirectAttributes ra) {
        try {
            horaireService.delete(id);

        } catch (NotFoundException e) {

        }
        ra.addFlashAttribute("message", "l'horaire' a été supprimé ");
        return "redirect:/restaurateur/restaurant/list";
    }


    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") Long id, Model model) {
        model.addAttribute("resto", horaireService.get(id));
        return "/restaurateur/restaurant/update.html";
    }


    @PostMapping("/update")
    public String updateHoraire(@Valid @ModelAttribute("resto") PatchHoraireIn patchHoraireIn, BindingResult bindingResult, RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return "/restaurateur/horaire/update";
        }

        horaireService.patch(patchHoraireIn.getId(), patchHoraireIn);
        ra.addFlashAttribute("message", "l'horaire' a été modifié  ");

        return "redirect:/restaurateur/horaire/list";
    }
}
