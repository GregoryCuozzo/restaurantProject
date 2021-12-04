package com.example.resthony.services.details;

import com.example.resthony.model.dto.visitor.CreateVisitorIn;
import com.example.resthony.model.dto.visitor.PatchVisitorIn;
import com.example.resthony.model.dto.visitor.VisitorOut;
import com.example.resthony.model.entities.Visitor;
import com.example.resthony.repositories.VisitorRepository;


import com.example.resthony.services.principal.VisitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VisitorDetailsServiceImpl implements VisitorService {
    @Autowired
    private VisitorRepository visitorRepository;

    public VisitorDetailsServiceImpl(VisitorRepository visitorRepository) {this.visitorRepository = visitorRepository;}

    @Override
    public VisitorOut get(Long id) {
        Visitor visitor = visitorRepository.findById(id).orElse(null);

        if(visitor == null) return null;

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
    public VisitorOut create(CreateVisitorIn createVisitorIn) {
        Visitor visitor = convertVisitorInToVisitorEntity(createVisitorIn);
        System.out.println(visitor);
        Visitor newVisitor = visitorRepository.save(visitor);
        return convertVisitorEntityToVisitorOut(newVisitor);
    }

    @Override
    public VisitorOut patch(Long id, PatchVisitorIn patchVisitorIn) {
        visitorRepository.updateVisitor(
                patchVisitorIn.getLastname(),
                patchVisitorIn.getFirstname(),
                patchVisitorIn.getEmail(),
                patchVisitorIn.getPhone(),
                id

        );
        Visitor visitorEntity = visitorRepository.getById(id);
        return convertVisitorEntityToVisitorOut(visitorEntity);
    }

    private VisitorOut convertVisitorEntityToVisitorOut(Visitor visitor) {

        VisitorOut visitorOut = VisitorOut.builder()
                .id(visitor.getId())
                .firstname(visitor.getFirstname())
                .lastname(visitor.getLastname())
                .email(visitor.getEmail())
                .resto(visitor.getResto())
                .phone(visitor.getPhone())
                .build();
        return visitorOut;
    }


    private Visitor convertVisitorInToVisitorEntity(CreateVisitorIn createVisitorIn) {
        Visitor visitor = Visitor.builder()
                .firstname(createVisitorIn.getFirstname())
                .lastname(createVisitorIn.getLastname())
                .email(createVisitorIn.getEmail())
                .resto(createVisitorIn.getResto())
                .phone(createVisitorIn.getPhone())
                .build();
        return visitor;
    }
}
