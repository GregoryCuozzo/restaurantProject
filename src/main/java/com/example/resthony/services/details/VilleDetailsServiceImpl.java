package com.example.resthony.services.details;


import com.example.resthony.model.dto.villes.CreateVilleIn;
import com.example.resthony.model.dto.villes.PatchVilleIn;
import com.example.resthony.model.dto.villes.VilleOut;
import com.example.resthony.model.entities.Ville;
import com.example.resthony.repositories.VilleRepository;
import com.example.resthony.repositories.VilleRepository;
import com.example.resthony.services.principal.VilleService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service

public class VilleDetailsServiceImpl implements VilleService {
    @Autowired
    private final VilleRepository villeRepository;

    public VilleDetailsServiceImpl( VilleRepository villerepository) {
        this.villeRepository = villerepository;

    }

    @Override
    public VilleOut get(Long id) {
        Ville ville = villeRepository.findById(id).orElse(null);

        if(ville == null) return null;

        VilleOut villeOut = convertVilleEntityToVilleOut(ville);

        return villeOut;
    }


    @Override
    public List<VilleOut> getAll() {
        List<Ville> villeEntities = villeRepository.findAll();

        List<VilleOut> villeOuts = new ArrayList<>();
        for (Ville ville : villeEntities) {
            villeOuts.add(convertVilleEntityToVilleOut(ville));
        }

        return villeOuts;
    }

    @Override
    public VilleOut create(CreateVilleIn createVilleIn) {
        Ville ville = convertVilleInToVilleEntity(createVilleIn);

        Ville newVille = villeRepository.save(ville);

        return convertVilleEntityToVilleOut(newVille);
    }




    @Override
    public VilleOut patch(Long id, PatchVilleIn patchVilleIn) {

        villeRepository.updateVille(
                patchVilleIn.getName(),
                patchVilleIn.getPays(),
                id
        );

        Ville villeEntity = villeRepository.getById(id);

        return convertVilleEntityToVilleOut(villeEntity);
    }

    @Override
    public void delete(Long id) throws NotFoundException {

        try {
            villeRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("No resto found", e);
        }

    }

    private VilleOut convertVilleEntityToVilleOut(Ville ville) {

        VilleOut villeOut = VilleOut.builder()
                .id(ville.getId())
                .name(ville.getName())
                .pays(ville.getPays())
                .build();
        return villeOut;
    }


    private Ville convertVilleInToVilleEntity(CreateVilleIn createVilleIn) {
        Ville ville = Ville.builder()
                .name(createVilleIn.getName())
                .pays(createVilleIn.getPays())
                .build();
        return ville;
    }

}
