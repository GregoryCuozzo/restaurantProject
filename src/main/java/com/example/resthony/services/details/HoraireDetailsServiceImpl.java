package com.example.resthony.services.details;

import com.example.resthony.model.dto.horaire.HoraireOut;
import com.example.resthony.model.dto.horaire.CreateHoraireIn;
import com.example.resthony.model.dto.horaire.PatchHoraireIn;
import com.example.resthony.model.entities.Horaire;
import com.example.resthony.repositories.HoraireRepository;
import com.example.resthony.services.principal.HoraireService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class HoraireDetailsServiceImpl implements HoraireService {
    private final HoraireRepository horaireRepository;

    @Autowired
    public HoraireDetailsServiceImpl(HoraireRepository horairerepository) {
        this.horaireRepository = horairerepository;
    }


    @Override
    public HoraireOut get(Long id) {
        Horaire horaire = horaireRepository.findById(id).orElse(null);

        if (horaire == null) return null;

        HoraireOut horaireOut = convertHoraireEntityToHoraireOut(horaire);

        return horaireOut;
    }


    @Override
    public List<HoraireOut> getAll() {
        List<Horaire> horaireEntities = horaireRepository.findAll();

        List<HoraireOut> horaireOuts = new ArrayList<>();
        for (Horaire horaire : horaireEntities) {
            horaireOuts.add(convertHoraireEntityToHoraireOut(horaire));
        }
        return horaireOuts;
    }


    @Override
    public HoraireOut create(CreateHoraireIn createHoraireIn) {
        Horaire horaire = convertHoraireInToHoraireEntity(createHoraireIn);
        Horaire newHoraire = horaireRepository.save(horaire);
        return convertHoraireEntityToHoraireOut(newHoraire);
    }

    @Override
    public HoraireOut patch(Long id, PatchHoraireIn patchHoraireIn) {
        horaireRepository.updateHoraire(
                patchHoraireIn.getOuverture(),
                patchHoraireIn.getFermeture(),
                id
        );

        Horaire horaireEntity = horaireRepository.getById(id);
        return convertHoraireEntityToHoraireOut(horaireEntity);
    }


    @Override
    public void delete(Long id) throws NotFoundException {

        try {
            horaireRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("No horaire found", e);
        }

    }

    @Override
    public List<HoraireOut> findByRestaurant(Long restaurant) {
        List<Horaire> horaireEntities = horaireRepository.findHoraireByRestaurant(restaurant);

        List<HoraireOut> horaireOuts = new ArrayList<>();
        for (Horaire horaire : horaireEntities) {
            horaireOuts.add(convertHoraireEntityToHoraireOut(horaire));
        }
        return horaireOuts;
    }

    private HoraireOut convertHoraireEntityToHoraireOut(Horaire horaire) {
        HoraireOut horaireOut = HoraireOut.builder()
                .id(horaire.getId())
                .restaurant(horaire.getRestaurant())
                .jour(horaire.getJour())
                .ouverture(horaire.getOuverture())
                .fermeture(horaire.getFermeture())
                .build();
        return horaireOut;
    }


    private Horaire convertHoraireInToHoraireEntity(CreateHoraireIn createHoraireIn) {
        Horaire horaire = Horaire.builder()
                .jour(createHoraireIn.getJour())
                .restaurant(createHoraireIn.getRestaurant())
                .ouverture(createHoraireIn.getOuverture())
                .fermeture(createHoraireIn.getFermeture())
                .build();
        return horaire;
    }


    public String checkDuplicateCreate(CreateHoraireIn createHoraireIn) {
        // Check duplicate
        String message = "";
        for (HoraireOut horaire : findByRestaurant(createHoraireIn.getRestaurant())) {
            if (Objects.equals(horaire.getJour(), createHoraireIn.getJour())) {
                message = "Il y a deja un horaire au jour sélectionné";
                break;
            }
        }
        return message;
    }


    public String checkDuplicateUpdate(PatchHoraireIn patchHoraireIn) {
        // Check duplicate
        String message = "";
        HoraireOut horairePatch = get(patchHoraireIn.id);

        for (HoraireOut horaire : findByRestaurant(horairePatch.restaurant)) {
            if (!Objects.equals(horaire.id, horairePatch.id)) {
                if (Objects.equals(horaire.jour, horairePatch.jour)) {
                    message = "Il y a deja un horaire au jour sélectionné";
                    break;
                }
            }
        }
        return message;
    }

}
