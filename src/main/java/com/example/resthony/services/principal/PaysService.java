package com.example.resthony.services.principal;

import com.example.resthony.model.dto.pays.CreatePaysIn;
import com.example.resthony.model.dto.pays.PatchPaysIn;
import com.example.resthony.model.dto.pays.PaysOut;
import javassist.NotFoundException;

import java.util.List;

public interface PaysService {
    PaysOut get(Long id);
    PaysOut create(CreatePaysIn createPaysIn);
    PaysOut patch(Long id, PatchPaysIn patchPaysIn);
    void delete(Long id) throws NotFoundException;
    List<PaysOut> getAll();
}
