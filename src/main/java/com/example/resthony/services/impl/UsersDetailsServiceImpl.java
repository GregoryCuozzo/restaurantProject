package com.example.resthony.services.impl;

import com.example.resthony.entities.User;
import com.example.resthony.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


@Service
public class UsersDetailsServiceImpl implements UserDetailsService {
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
    public List<User> listAll(){
        return userRepository.findAll();

    }

    public  void save(User user) {
        userRepository.save(user);
    }


    public  User get(Integer id) throws UserNotFoundException {
        Optional<User> result= userRepository.findById(id);
        if(result.isPresent()){
            return result.get();
        }
        throw new UserNotFoundException("aucun utilisateur existant avec cet identifiant  " + id);

    }

    public  void delete(Integer id) throws UserNotFoundException{
        Long count=  userRepository.countById(id);
        if (count == null || count == 0){
            throw new UserNotFoundException("aucun utilisateur existant avec cet identifiant" + id);
        }
        userRepository.deleteById(id);

    }
}
