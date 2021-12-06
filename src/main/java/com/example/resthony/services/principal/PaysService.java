package com.example.resthony.services.principal;


import com.example.resthony.model.dto.pays.PaysOut;
import javassist.NotFoundException;

import java.util.List;

public interface PaysService {
    PaysOut get(Long id);
    List<PaysOut> getAll();
}