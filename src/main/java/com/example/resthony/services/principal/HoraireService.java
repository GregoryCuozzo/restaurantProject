package com.example.resthony.services.principal;

import com.example.resthony.model.dto.horaire.CreateHoraireIn;
import com.example.resthony.model.dto.horaire.HoraireOut;
import com.example.resthony.model.dto.horaire.PatchHoraireIn;
import javassist.NotFoundException;

import java.util.List;

public interface HoraireService {
    HoraireOut get(Long id);

    List<HoraireOut> getAll();

    HoraireOut create(CreateHoraireIn createHoraireIn);

    HoraireOut patch(Long id, PatchHoraireIn patchHoraireIn);

    void delete(Long id) throws NotFoundException;
}
