package com.example.resthony.controller.restaurateur;

import com.example.resthony.model.dto.horaire.CreateHoraireIn;
import com.example.resthony.model.dto.horaire.HoraireOut;
import com.example.resthony.model.dto.horaire.PatchHoraireIn;

import com.example.resthony.services.principal.HoraireService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

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
        model.addAttribute("horaire", new CreateHoraireIn());
        return "/restaurateur/horaire/create.html";
    }

    @PostMapping("/create")
    public String createHoraire(@Valid @ModelAttribute("horaire") CreateHoraireIn createHoraireIn, BindingResult bindingResult, Model model, RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return "/restaurateur/horaire/create.html";
        }

        String message = horaireService.checkDuplicateCreate(createHoraireIn);

        if (!message.equals("")) {
            ra.addFlashAttribute("messageErreur", message);
            return "redirect:/restaurateur/horaire/create/" + createHoraireIn.restaurant;
        }

        try {
            horaireService.create(createHoraireIn);
        } catch (Exception e) {
            ra.addFlashAttribute("messageErreur", "Problème dans la création de l'horaire.");
            return "redirect:/restaurateur/horaire/list/" + createHoraireIn.restaurant;
        }

        model.addAttribute("horaires", horaireService.findByRestaurant(createHoraireIn.restaurant));
        return "redirect:/restaurateur/horaire/list/" + createHoraireIn.restaurant;
    }


    @GetMapping("/delete/{id}")
    public String deleteHoraire(@PathVariable("id") Long id, RedirectAttributes ra) {
        HoraireOut horaire = horaireService.get(id);
        Long resto = horaire.id;
        System.out.printf("horaire");
        try {
            horaireService.delete(id);

        } catch (NotFoundException e) {
            ra.addFlashAttribute("messageErreur", "Pas d'horaire trouvé avec l'id "+id);
            return "redirect:/restaurateur/horaire/list/" + horaire.restaurant;
        }

        ra.addFlashAttribute("message", "L'horaire' a été supprimé ");
        return "redirect:/restaurateur/horaire/list/" + Long.toString(horaire.restaurant);
    }


    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") Long id, Model model) {
        model.addAttribute("horaire", horaireService.get(id));
        return "/restaurateur/horaire/update.html";
    }


    @PostMapping("/update")
    public String updateHoraire(@Valid @ModelAttribute("horaire") PatchHoraireIn patchHoraireIn, BindingResult bindingResult, RedirectAttributes ra, Model model) {
        if (bindingResult.hasErrors()) {
            return "/restaurateur/horaire/update/" + patchHoraireIn.id;
        }

        String message = horaireService.checkDuplicateUpdate(patchHoraireIn);
        HoraireOut horaire = horaireService.get(patchHoraireIn.id);

        if (!message.equals("")) {
            ra.addFlashAttribute("messageErreur", message);
            return "redirect:/restaurateur/horaire/update/" + patchHoraireIn.id;
        }

        try{
            horaireService.patch(patchHoraireIn.getId(), patchHoraireIn);
        }
        catch (Exception e){
            ra.addFlashAttribute("messageErreur", "Problème avec la modification des horaires du restaurant " + horaire.getRestaurant());
            return "redirect:/restaurateur/horaire/list/" + horaire.restaurant;
        }

        ra.addFlashAttribute("message", "l'horaire' a été modifié  ");

        model.addAttribute("resto", horaire.restaurant);
        return "redirect:/restaurateur/horaire/list/" + horaire.restaurant;
    }
}
