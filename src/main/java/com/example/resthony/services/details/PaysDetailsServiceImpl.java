package com.example.resthony.services.details;
import com.example.resthony.model.dto.pays.PaysOut;
import com.example.resthony.model.entities.Pays;
import com.example.resthony.repositories.PaysRepository;
import com.example.resthony.services.principal.PaysService;
import org.springframework.beans.factory.annotation.Autowired;
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



    private PaysOut convertPaysEntityToPaysOut(Pays pays) {

        PaysOut paysOut = PaysOut.builder()
                .id(pays.getId())
                .name(pays.getName())

                .build();
        return paysOut;
    }




}
