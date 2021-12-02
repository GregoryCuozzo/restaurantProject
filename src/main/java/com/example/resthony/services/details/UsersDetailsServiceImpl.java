package com.example.resthony.services.details;

import com.example.resthony.model.dto.user.CreateUserIn;
import com.example.resthony.model.dto.user.PatchUserIn;
import com.example.resthony.model.dto.user.UserOut;
import com.example.resthony.model.entities.User;
import com.example.resthony.repositories.UserRepository;
import com.example.resthony.services.principal.UserService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;


@Service
public class UsersDetailsServiceImpl implements UserDetailsService, UserService {
    @Autowired
    private  UserRepository userRepository;

    public UsersDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException("No user found with username : " + username);
        } else {
            return user;
        }
    }




    @Override
    public UserOut get(Long id) {
        User user = userRepository.findById(id).orElse(null);

        if(user == null) return null;

        UserOut userOut = convertUserEntityToUserOut(user);

        return userOut;
    }

    @Override
    public List<UserOut> getAll() {
        List<User> userEntities = userRepository.findAll();

        List<UserOut> userOuts = new ArrayList<>();
        for (User user : userEntities) {
            userOuts.add(convertUserEntityToUserOut(user));
        }

        return userOuts;
    }

    @Override
    public UserOut findByUsername(String username){
       User user = userRepository.findByUsername(username);

        if(user == null) return null;;
        UserOut userOut = convertUserEntityToUserOut(user);
        return userOut;
    }




    @Override
    public UserOut create(CreateUserIn createUserIn) {
        User user = convertUserInToUserEntity(createUserIn);
        System.out.println(user);
        User newUser = userRepository.save(user);
       return convertUserEntityToUserOut(newUser);
    }


    @Override
    public UserOut register(CreateUserIn createUserIn) {
        User user = convertUserInToUserEntity(createUserIn);
        user.setUserRole();
        User newUser = userRepository.save(user);
        return convertUserEntityToUserOut(newUser);
    }


    @Override
    public UserOut patch(Long id, PatchUserIn patchUserIn) {
        userRepository.updateUser(
                patchUserIn.getUsername(),
                patchUserIn.getLastname(),
                patchUserIn.getFirstname(),
                patchUserIn.getEmail(),
                id

        );
        User userEntity = userRepository.getById(id);
        return convertUserEntityToUserOut(userEntity);
    }

    @Override
    public UserOut updatePass(Long id, String password){
        userRepository.updatePass(id,password);
        User userEntity = userRepository.getById(id);
        return convertUserEntityToUserOut(userEntity);
    }

    @Override
    public void delete(Long id) throws NotFoundException {

        try{
            userRepository.deleteById(id);
        }catch (EmptyResultDataAccessException e){
            throw new NotFoundException("utilisateur non existant",e);
        }

    }



    private UserOut convertUserEntityToUserOut(User user) {

        UserOut userOut = UserOut.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .resto(user.getResto())
                .roles(user.getRoles())
                .build();
        return userOut;
    }


    private User convertUserInToUserEntity(CreateUserIn createUserIn) {
        User user = User.builder()
                .username(createUserIn.getUsername())
                .firstname(createUserIn.getFirstname())
                .lastname(createUserIn.getLastname())
                .email(createUserIn.getEmail())
                .resto(createUserIn.getResto())
                .password(createUserIn.getPassword())
                .roles(createUserIn.getRoles())
                .accountNonExpired(true)
                .accountNonLocked(true)
                .enabled(true)
                .credentialsNonExpired(true)
                .build();
        return user;
    }

    @Override
    public User getCurrentUser(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "";

        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        return userRepository.findByUsername(username);
    }

}
