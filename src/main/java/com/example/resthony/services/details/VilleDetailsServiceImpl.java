package com.example.resthony.services.details;


import com.example.resthony.model.dto.villes.CreateVilleIn;
import com.example.resthony.model.dto.villes.PatchVilleIn;
import com.example.resthony.model.dto.villes.VilleOut;
import com.example.resthony.model.entities.Ville;
import com.example.resthony.repositories.VilleRepositary;
import com.example.resthony.services.principal.VilleService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.ArrayList;
import java.util.List;

public class VilleDetailsServiceImpl implements VilleService {
    @Autowired
    private final VilleRepositary villeRepositary;

    public VilleDetailsServiceImpl( VilleRepositary villerepositary) {
        this.villeRepositary = villerepositary;

    }

    @Override
    public VilleOut get(Long id) {
        Ville ville = villeRepositary.findById(id).orElse(null);

        if(ville == null) return null;

        VilleOut villeOut = convertVilleEntityToVilleOut(ville);

        return villeOut;
    }


    @Override
    public List<VilleOut> getAll() {
        List<Ville> villeEntities = villeRepositary.findAll();

        List<VilleOut> villeOuts = new ArrayList<>();
        for (Ville ville : villeEntities) {
            villeOuts.add(convertVilleEntityToVilleOut(ville));
        }

        return villeOuts;
    }

    @Override
    public VilleOut create(CreateVilleIn createVilleIn) {
        Ville ville = convertVilleInToVilleEntity(createVilleIn);

        Ville newVille = villeRepositary.save(ville);

        return convertVilleEntityToVilleOut(newVille);
    }




    @Override
    public VilleOut patch(Long id, PatchVilleIn patchVilleIn) {

        villeRepositary.updateVille(
                patchVilleIn.getNom(),
                patchVilleIn.getPays(),
                id
        );

        Ville villeEntity = villeRepositary.getById(id);

        return convertVilleEntityToVilleOut(villeEntity);
    }

    @Override
    public void delete(Long id) throws NotFoundException {

        try {
            villeRepositary.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("No resto found", e);
        }

    }

    private VilleOut convertVilleEntityToVilleOut(Ville ville) {

        VilleOut villeOut = VilleOut.builder()
                .id(ville.getId())
                .nom(ville.getNom())
                .pays(ville.getPays())
                .build();
        return villeOut;
    }


    private Ville convertVilleInToVilleEntity(CreateVilleIn createVilleIn) {
        Ville ville = Ville.builder()
                .nom(createVilleIn.getNom())
                .build();
        return ville;
    }

}
