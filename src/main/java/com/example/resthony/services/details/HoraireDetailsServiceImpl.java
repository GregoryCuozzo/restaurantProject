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

    public List<String> horaireFiltre(Long id) {
        Horaire horaireOut = horaireRepository.getById(id);
        List<String> horaireVue = new ArrayList<>();
        String horaireDebut = horaireOut.getOuverture();
        String horaireFin = horaireOut.getFermeture();
        horaireVue.add(horaireDebut);
        String minuteDebutString = new StringBuilder().append(horaireDebut.charAt(3)).append(horaireDebut.charAt(4)).toString();
        String heureDebutString = new StringBuilder().append(horaireDebut.charAt(0)).append(horaireDebut.charAt(1)).toString();
        String heureFinString = new StringBuilder().append(horaireFin.charAt(0)).append(horaireFin.charAt(1)).toString();
        Long heureDebutLong = Long.valueOf(heureDebutString);
        Long heureFinLong = Long.valueOf(heureFinString);
        Long diff = heureFinLong - heureDebutLong;
        String horaire = horaireDebut;
        Long heure = heureDebutLong;
        for (int i = 0; i < diff; i++) {
            String minutes = "00";
            if (!horaire.equals(horaireFin)) {
                if (!horaire.equals(horaireDebut)) {
                    heure = heure + 1;
                }
                for (int j = 0; j < 4; j++) {
                    if (j == 0) {
                        if (minuteDebutString.equals("45")) {
                            minuteDebutString = "";
                            break;
                        } else if (minuteDebutString.equals("00")) {
                            horaire = heure.toString() + ":" + "15";
                            horaireVue.add(horaire);
                        } else if (minuteDebutString.equals("15")) {
                            horaire = heure.toString() + ":" + "30";
                            horaireVue.add(horaire);
                        } else if (minuteDebutString.equals("30")) {
                            minuteDebutString = "";
                            horaire = heure.toString() + ":" + "45";
                            horaireVue.add(horaire);
                            break;
                        }
                    } else {
                        if (minutes.equals("00") && !horaire.equals(horaireFin)) {
                            horaire = heure.toString() + ":" + "00";
                            horaireVue.add(horaire);
                            horaire = heure.toString() + ":" + "15";
                            horaireVue.add(horaire);
                            minutes = "15";
                        } else if (minutes.equals("15") && !horaire.equals(horaireFin)) {
                            horaire = heure.toString() + ":" + "30";
                            horaireVue.add(horaire);
                            minutes = "30";
                        } else if (minutes.equals("30") && !horaire.equals(horaireFin)) {
                            horaire = heure.toString() + ":" + "45";
                            horaireVue.add(horaire);
                            minutes = "45";
                        }
                    }
                }
            }
        }
        horaireVue.add(horaireFin);
        System.out.println(horaireVue);
        return horaireVue;
    }


}
