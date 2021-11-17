package com.example.resthony.services.impl;

import com.example.resthony.controller.restaurants.dto.CreateRestoIn;
import com.example.resthony.controller.restaurants.dto.PatchRestoIn;
import javassist.NotFoundException;
import com.example.resthony.controller.restaurants.dto.RestoOut;

import java.util.List;

public interface RestoService {
    RestoOut get(Integer id);
    RestoOut create(CreateRestoIn createRestoIn);
    RestoOut patch(Integer id, PatchRestoIn patchRestoIn);
    void delete(Integer id) throws NotFoundException;
    List<RestoOut> getAll();
}
