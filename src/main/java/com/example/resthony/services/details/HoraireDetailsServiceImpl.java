package com.example.resthony.services.details;

import com.example.resthony.model.dto.horaire.PatchHoraireIn;
import com.example.resthony.model.dto.horaire.HoraireOut;
import com.example.resthony.model.dto.horaire.CreateHoraireIn;
import com.example.resthony.model.entities.Horaire;
import com.example.resthony.repositories.HoraireRepository;
import com.example.resthony.services.principal.HoraireService;
import org.springframework.beans.factory.annotation.Autowired;

public class HoraireDetailsServiceImpl implements HoraireService {
    private final HoraireRepository horaireRepository;

    @Autowired
    public HoraireDetailsServiceImpl(HoraireRepository horairerepository) {
        this.horaireRepository = horairerepository;
    }

    private HoraireOut convertHoraireEntityToHoraireOut(Horaire horaire) {
        HoraireOut horaireOut = HoraireOut.builder()
                .id(horaire.getId())
                .jour(horaire.getJour())
                .ouverture(horaire.getOuverture())
                .fermeture(horaire.getFermeture())
                .build();
        return horaireOut;
    }


    private Horaire convertHorairInToHoraireEntity(CreateHoraireIn createHoraireIn) {
        Horaire horaire = Horaire.builder()
                .jour(createHoraireIn.getJour())
                .ouverture(createHoraireIn.getOuverture())
                .fermeture(createHoraireIn.getFermeture())
                .build();
        return horaire;
    }

}
