package com.example.resthony.services.principal;

import com.example.resthony.model.dto.pays.CreatePaysIn;
import com.example.resthony.model.dto.pays.PatchPaysIn;
import com.example.resthony.model.dto.pays.PaysOut;
import com.example.resthony.model.dto.villes.CreateVilleIn;
import com.example.resthony.model.dto.villes.PatchVilleIn;
import com.example.resthony.model.dto.villes.VilleOut;
import javassist.NotFoundException;

import java.util.List;

public interface VilleService {

    VilleOut get(Long id);
    VilleOut create(CreateVilleIn createVilleIn);
    VilleOut patch(Long id, PatchVilleIn patchVilleIn);
    void delete(Long id) throws NotFoundException;
    List<VilleOut> getAll();
}
