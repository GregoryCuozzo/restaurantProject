package com.example.resthony.services.details;

import com.example.resthony.model.dto.visitor.CreateVisitorIn;
import com.example.resthony.model.dto.visitor.PatchVisitorIn;
import com.example.resthony.model.dto.visitor.VisitorOut;
import com.example.resthony.model.entities.Restaurant;
import com.example.resthony.model.entities.Visitor;
import com.example.resthony.repositories.RestauRepository;
import com.example.resthony.repositories.VisitorRepository;
import com.example.resthony.services.principal.VisitorService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class VisitorDetailsServiceImpl implements VisitorService {
    private final VisitorRepository visitorRepository;
    private final RestauRepository restauRepository;

    @Autowired
    public VisitorDetailsServiceImpl(VisitorRepository visitorRepository, RestauRepository restauRepository) {
        this.visitorRepository = visitorRepository;
        this.restauRepository = restauRepository;
    }

    @Override
    public VisitorOut get(Long id) {
        Visitor visitor = visitorRepository.findById(id).orElse(null);

        if (visitor == null) return null;

        VisitorOut visitorOut = convertVisitorEntityToVisitorOut(visitor);

        return visitorOut;
    }

    @Override
    public List<VisitorOut> getAll() {
        List<Visitor> visitorEntities = visitorRepository.findAll();

        List<VisitorOut> visitorOuts = new ArrayList<>();
        for (Visitor visitor : visitorEntities) {
            visitorOuts.add(convertVisitorEntityToVisitorOut(visitor));
        }

        return visitorOuts;
    }

    @Override
    public void deleteVisitor(Long id) throws NotFoundException {

        try{
            visitorRepository.deleteById(id);
        }catch (EmptyResultDataAccessException e){
            throw new NotFoundException("utilisateur non existant",e);
        }

    }

    @Override
    public VisitorOut create(CreateVisitorIn createVisitorIn) {
        Visitor visitor = convertVisitorInToVisitorEntity(createVisitorIn);
        Visitor newVisitor = visitorRepository.save(visitor);
        return convertVisitorEntityToVisitorOut(newVisitor);
    }

    @Override
    public VisitorOut patch(Long id, PatchVisitorIn patchVisitorIn) {

        Visitor visitorToUpdate = Visitor.builder()
                .id(patchVisitorIn.getId())
                .firstname(patchVisitorIn.getFirstname())
                .lastname(patchVisitorIn.getLastname())
                .email(patchVisitorIn.getEmail())
                .phone(patchVisitorIn.getPhone())
                .date(patchVisitorIn.getDate())
                .time(patchVisitorIn.getTime())
                .nbcouverts(patchVisitorIn.getNbcouverts())
                .resto(restauRepository.findByName(patchVisitorIn.getResto()))
                .build();
        visitorRepository.save(visitorToUpdate);
        Visitor visitorEntity = visitorRepository.getById(id);
        return convertVisitorEntityToVisitorOut(visitorEntity);
    }

    private VisitorOut convertVisitorEntityToVisitorOut(Visitor visitor) {

        VisitorOut visitorOut = VisitorOut.builder()
                .id(visitor.getId())
                .firstname(visitor.getFirstname())
                .lastname(visitor.getLastname())
                .email(visitor.getEmail())
                .resto(visitor.getResto().getName())
                .phone(visitor.getPhone())
                .date(visitor.getDate())
                .time(visitor.getTime())
                .nbcouverts(visitor.getNbcouverts())
                .build();
        return visitorOut;
    }


    private Visitor convertVisitorInToVisitorEntity(CreateVisitorIn createVisitorIn) {
        Restaurant restaurant = restauRepository.findByName(createVisitorIn.getResto());
        Visitor visitor = Visitor.builder()
                .firstname(createVisitorIn.getFirstname())
                .lastname(createVisitorIn.getLastname())
                .email(createVisitorIn.getEmail())
                .resto(restaurant)
                .phone(createVisitorIn.getPhone())
                .date(createVisitorIn.getDate())
                .time(createVisitorIn.getTime())
                .nbcouverts(createVisitorIn.getNbcouverts())
                .build();
        return visitor;
    }
}
