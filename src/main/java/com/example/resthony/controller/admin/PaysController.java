package com.example.resthony.controller.admin;


import com.example.resthony.model.dto.pays.CreatePaysIn;
import com.example.resthony.model.dto.pays.PatchPaysIn;
import com.example.resthony.services.principal.PaysService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin/pays")
public class PaysController {
    private final PaysService paysService;

    @Autowired
    public PaysController(PaysService paysService) {
        this.paysService = paysService;
    }

    @GetMapping("/list")
    public String all(Model model){
        model.addAttribute("pays",paysService.getAll());
        return "pays/pays.html";

    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("pays", new CreatePaysIn());
        return "pays/create.html";
    }

    @PostMapping("/create")
    public String createPays(@Valid @ModelAttribute("pays") CreatePaysIn createPaysIn, BindingResult bindingResult, RedirectAttributes ra) {
        if(bindingResult.hasErrors()) {
            return "/create";
        }

        paysService.create(createPaysIn);
        ra.addFlashAttribute("message", "le Pays à été rajouté ");

        return "redirect:/admin/pays/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteResto(@PathVariable("id") Long id, RedirectAttributes ra) {
        try {
            paysService.delete(id);

        } catch (NotFoundException e) {

        }
        ra.addFlashAttribute("message", "Le pays a été supprimé ");
        return "redirect:/admin/pays/list";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") String id, Model model) {
        model.addAttribute("pays", paysService.get(Long.valueOf(id)));
        return "pays/update.html";
    }

    @PostMapping("/update")
    public String updateResto(@Valid @ModelAttribute("pays") PatchPaysIn patchPaysIn, BindingResult bindingResult,RedirectAttributes ra) {
        if(bindingResult.hasErrors()) {
            return "/update";
        }

        paysService.patch(patchPaysIn.getId(), patchPaysIn);
        ra.addFlashAttribute("message", "le pays a été modifié  ");

        return "redirect:/admin/pays/list";
    }



}
