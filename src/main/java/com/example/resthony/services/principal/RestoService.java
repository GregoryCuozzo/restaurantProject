package com.example.resthony.services.principal;

import com.example.resthony.model.dto.restaurant.CreateRestoIn;
import com.example.resthony.model.dto.restaurant.PatchRestoIn;
import javassist.NotFoundException;
import com.example.resthony.model.dto.restaurant.RestoOut;

import java.util.List;

public interface RestoService {
    RestoOut get(Long id);
    RestoOut create(CreateRestoIn createRestoIn);
    RestoOut patch(Long id, PatchRestoIn patchRestoIn);
    void delete(Long id) throws NotFoundException;
    List<RestoOut> getAll();
}
