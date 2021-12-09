package com.example.resthony.controller.restaurateur;

import com.example.resthony.model.dto.villes.CreateVilleIn;
import com.example.resthony.model.dto.villes.PatchVilleIn;
import com.example.resthony.services.principal.PaysService;
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
@RequestMapping("/restaurateur/villes")
public class VilleController {
    private final VilleService villeService;
    private final PaysService paysService;

    @Autowired
    public VilleController(VilleService villeService, PaysService paysService) {
        this.villeService = villeService;
        this.paysService = paysService;
    }

    @GetMapping("/list")
    public String all(Model model){
        model.addAttribute("villes", villeService.getAll());
        model.addAttribute("pays", paysService.getAll());
        return "/restaurateur/villes/villes.html";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("ville", new CreateVilleIn());
        model.addAttribute("pays", paysService.getAll());
        return "/restaurateur/villes/create.html";
    }

    @PostMapping("/create")
    public String createVille(@Valid @ModelAttribute("ville") CreateVilleIn createVilleIn, BindingResult bindingResult, Model model, RedirectAttributes ra) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("pays", paysService.getAll());
            return "/restaurateur/villes/create.html";
        }

        try {
            villeService.create(createVilleIn);
        }
        catch(Exception e){
            ra.addFlashAttribute("messageErreur", "Problème avec la création de la ville.");
            return "redirect:/restaurateur/villes/list";
        }


        return "redirect:/restaurateur/villes/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteVille(@PathVariable("id") Long id, RedirectAttributes ra) {
        try {
            villeService.delete(id);

        } catch (NotFoundException e) {
            ra.addFlashAttribute("messageErreur", "Pas de ville trouvée avec l'id "+ id);
            return "redirect:/restaurateur/villes/list";
        }
        ra.addFlashAttribute("message", "La ville a été supprimée ");
        return "redirect:/restaurateur/villes/list";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") String id, Model model) {
        model.addAttribute("ville", villeService.get(Long.valueOf(id)));
        model.addAttribute("pays", paysService.getAll());
        return "restaurateur/villes/update.html";
    }

    @PostMapping("/update")
    public String updateResto(@Valid @ModelAttribute("ville") PatchVilleIn patchVilleIn, BindingResult bindingResult,Model model, RedirectAttributes ra) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("pays", paysService.getAll());
            return "restaurateur/villes/update/" + patchVilleIn.getId();
        }

        try {
            villeService.patch(patchVilleIn.getId(), patchVilleIn);
        }
        catch(Exception e){
            ra.addFlashAttribute("messageErreur", "Problème avec la modification de la ville.");
            return "redirect:/restaurateur/villes/list";
        }

        return "redirect:/restaurateur/villes/list";
    }



}
