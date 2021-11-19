package com.example.resthony.controller.pays;


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
        return "pays.html";

    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("resto", new CreatePaysIn());
        return "create.html";
    }

    @PostMapping("/create")
    public String createPays(@Valid @ModelAttribute("pays") CreatePaysIn createPaysIn, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "/create";
        }

        paysService.create(createPaysIn);

        return "redirect:/web/resto/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteResto(@PathVariable("id") Long id, RedirectAttributes ra) {
        try {
            paysService.delete(id);

        } catch (NotFoundException e) {

        }
        ra.addFlashAttribute("message", "l'utilisateur a été supprimé ");
        return "redirect:/admin/restaurant/list";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") String id, Model model) {
        model.addAttribute("resto", paysService.get(Long.valueOf(id)));
        return "update.html";
    }

    @PostMapping("/update")
    public String updateResto(@Valid @ModelAttribute("resto") PatchPaysIn patchPaysIn, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "/update";
        }

        paysService.patch(patchPaysIn.getId(), patchPaysIn);

        return "redirect:/web/resto/list";
    }



}
