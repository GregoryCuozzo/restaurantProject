package com.example.resthony.controller.admin;

import com.example.resthony.model.dto.villes.CreateVilleIn;
import com.example.resthony.model.dto.villes.PatchVilleIn;
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
@RequestMapping("/admin/villes")
public class VilleController {
    private final VilleService villeService;

    @Autowired
    public VilleController(VilleService villeService) {
        this.villeService = villeService;
    }

    @GetMapping("/list")
    public String all(Model model){
        model.addAttribute("villes", villeService.getAll());
        return "villes/villes.html";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("ville", new CreateVilleIn());
        return "create.html";
    }

    @PostMapping("/create")
    public String createVille(@Valid @ModelAttribute("ville") CreateVilleIn createVilleIn, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "/create";
        }

        villeService.create(createVilleIn);

        return "redirect:/admin/villes/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteVille(@PathVariable("id") Long id, RedirectAttributes ra) {
        try {
            villeService.delete(id);

        } catch (NotFoundException e) {

        }
        ra.addFlashAttribute("message", "La ville a été supprimée ");
        return "redirect:/admin/villes/list";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") String id, Model model) {
        model.addAttribute("ville", villeService.get(Long.valueOf(id)));
        return "ville/update.html";
    }

    @PostMapping("/update")
    public String updateResto(@Valid @ModelAttribute("ville") PatchVilleIn patchVilleIn, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "/update";
        }

        villeService.patch(patchVilleIn.getId(), patchVilleIn);

        return "redirect:/admin/villes/list";
    }



}
