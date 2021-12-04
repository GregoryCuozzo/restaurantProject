package com.example.resthony.services.principal;

import com.example.resthony.model.dto.visitor.CreateVisitorIn;
import com.example.resthony.model.dto.visitor.PatchVisitorIn;
import com.example.resthony.model.dto.visitor.CreateVisitorIn;
import com.example.resthony.model.dto.visitor.VisitorOut;
import javassist.NotFoundException;

import java.util.List;

public interface VisitorService {
    VisitorOut get(Long id);
    VisitorOut create(CreateVisitorIn createVisitorIn);
    VisitorOut patch(Long id, PatchVisitorIn patchVisitorIn);
    List<VisitorOut> getAll();
}
