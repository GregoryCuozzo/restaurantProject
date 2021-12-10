package com.example.resthony.services.principal;

import com.example.resthony.model.dto.horaire.CreateHoraireIn;
import com.example.resthony.model.dto.horaire.HoraireOut;
import com.example.resthony.model.dto.horaire.PatchHoraireIn;
import com.example.resthony.model.dto.user.CreateUserIn;
import com.example.resthony.model.dto.user.PatchUserIn;
import javassist.NotFoundException;


import java.util.List;

public interface HoraireService {
    HoraireOut get(Long id);

    List<HoraireOut> getAll();

    HoraireOut create(CreateHoraireIn createHoraireIn);

    HoraireOut patch(Long id, PatchHoraireIn patchHoraireIn);

    void delete(Long id) throws NotFoundException;

    List<HoraireOut> findByRestaurant(Long restaurant);

    String checkDuplicateCreate(CreateHoraireIn createHoraireIn);

    String checkDuplicateUpdate(PatchHoraireIn patchHoraireIn);

    List<String> horaireFiltre(Long id);
}
