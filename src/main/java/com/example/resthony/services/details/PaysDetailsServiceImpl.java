package com.example.resthony.services.details;

import com.example.resthony.model.dto.pays.CreatePaysIn;
import com.example.resthony.model.dto.pays.PatchPaysIn;
import com.example.resthony.model.dto.pays.PaysOut;
import com.example.resthony.model.entities.Pays;
import com.example.resthony.repositories.PaysRepository;
import com.example.resthony.services.principal.PaysService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaysDetailsServiceImpl implements PaysService {
    @Autowired
    private final PaysRepository paysRepository;

    public PaysDetailsServiceImpl(PaysRepository paysRepository) {
        this.paysRepository = paysRepository;
    }

    @Override
    public PaysOut get(Long id) {
        Pays pays = paysRepository.findById(id).orElse(null);

        if(pays == null) return null;

        PaysOut paysOut = convertPaysEntityToPaysOut(pays);

        return paysOut;
    }


    @Override
    public List<PaysOut> getAll() {
        List<Pays> paysEntities = paysRepository.findAll();

        List<PaysOut> paysOuts = new ArrayList<>();
        for (Pays pays : paysEntities) {
            paysOuts.add(convertPaysEntityToPaysOut(pays));
        }

        return paysOuts;
    }

    @Override
    public PaysOut create(CreatePaysIn createPaysIn) {
        Pays pays = convertPaysInToPaysEntity(createPaysIn);

        Pays newPays = paysRepository.save(pays);

        return convertPaysEntityToPaysOut(newPays);
    }


    @Override
    public PaysOut patch(Long id, PatchPaysIn patchPaysIn) {

        paysRepository.updatePays(
                patchPaysIn.getNom(),
                id
        );

        Pays paysEntity = paysRepository.getById(id);

        return convertPaysEntityToPaysOut(paysEntity);
    }

    @Override
    public void delete(Long id) throws NotFoundException {

        try {
            paysRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("No resto found", e);
        }

    }

    private PaysOut convertPaysEntityToPaysOut(Pays pays) {

        PaysOut paysOut = PaysOut.builder()
                .id(pays.getId())
                .nom(pays.getNom())

                .build();
        return paysOut;
    }


    private Pays convertPaysInToPaysEntity(CreatePaysIn createPaysIn) {
        Pays pays = Pays.builder()
                .nom(createPaysIn.getNom())
                .build();
        return pays;
    }

}
