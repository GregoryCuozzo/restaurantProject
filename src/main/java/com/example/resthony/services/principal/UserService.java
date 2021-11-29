package com.example.resthony.services.principal;

import com.example.resthony.model.dto.restaurant.CreateRestoIn;
import com.example.resthony.model.dto.restaurant.PatchRestoIn;
import com.example.resthony.model.dto.restaurant.RestoOut;
import com.example.resthony.model.dto.user.CreateUserIn;
import com.example.resthony.model.dto.user.PatchUserIn;
import com.example.resthony.model.dto.user.UserOut;
import com.example.resthony.model.entities.User;
import javassist.NotFoundException;

import java.util.List;

public interface UserService {
    UserOut get(Long id);
    UserOut create(CreateUserIn createUserIn);
    UserOut register(CreateUserIn createUserIn);
    UserOut patch(Long id, PatchUserIn patchUserIn);
    void delete(Long id) throws NotFoundException, UserNotFoundException;
    List<UserOut> getAll();
    UserOut findByUsername(String username);
    User getCurrentUser();

}
