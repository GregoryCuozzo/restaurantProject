package com.example.resthony.services.impl;

import com.example.resthony.entities.Restaurant;
import com.example.resthony.entities.User;
import com.example.resthony.repositories.RestauRepository;
import com.example.resthony.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class RestauDetailsServiceImpl {
    @Autowired
    private RestauRepository RestauRepository;

    public RestauDetailsServiceImpl(RestauRepository restauRepository) {
        this.RestauRepository = RestauRepository;
    }

    public List<Restaurant> listAll(){
        return RestauRepository.findAll();

    }

    public  Restaurant get(Integer id) throws UserNotFoundException {
        Optional<Restaurant> result= RestauRepository.findById(id);
        if(result.isPresent()){
            return result.get();
        }
        throw new UserNotFoundException("aucun restaurant existant avec cet identifiant  " + id);

    }

    public  void delete(Integer id) throws UserNotFoundException{
        Long count=  RestauRepository.countById(id);
        if (count == null || count == 0){
            throw new UserNotFoundException("aucun restaurant existant avec cet identifiant" + id);
        }
        RestauRepository.deleteById(id);

    }
    
    
}


