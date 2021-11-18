package com.example.resthony.services.impl;

import com.example.resthony.entities.Restaurant;
import com.example.resthony.entities.User;
import com.example.resthony.repositories.RestauRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import java.util.List;
import java.util.Optional;

@Service
public class RestauDetailsServiceImpl implements RestoService {
    private final RestauRepository restauRepository;

    @Autowired
    public RestauDetailsServiceImpl(RestauRepository restauRepository){this.restauRepository = restauRepository;}

    @Override
    public RestoOut get(Integer id) {
        Restaurant restaurant = restauRepository.findById(id).orElse(null);

        if(restaurant == null) return null;

        RestoOut restoOut = convertRestoEntityToRestoOut(restaurant);

        return restoOut;
    }

    @Override
    public List<RestoOut> getAll() {
        List<Restaurant> restoEntities = restauRepository.findAll();

        //List<RestoOut> restoOuts = restoEntities.stream().map(restoEntity -> convertRestoEntityToRestoOut(restoEntity)).collect(Collectors.toList());
        //List<RestoOut> restoOuts = restoEntities.stream().map(this::convertRestoEntityToRestoOut).collect(Collectors.toList());

        List<RestoOut> restoOuts = new ArrayList<>();
        for (Restaurant restaurant : restoEntities) {
            restoOuts.add(convertRestoEntityToRestoOut(restaurant));
        }

        return restoOuts;
    }

    @Override
    public RestoOut create(CreateRestoIn createRestoIn) {
        Restaurant restaurant = convertRestoInToRestoEntity(createRestoIn);

        Restaurant newRestaurant = restauRepository.save(restaurant);

        return convertRestoEntityToRestoOut(newRestaurant);
    }

    @Override
    public RestoOut patch(Integer id, PatchRestoIn patchRestoIn) {

        restauRepository.updateResto(
                patchRestoIn.getNom(),
                patchRestoIn.getAdress(),
                patchRestoIn.getNbPlace(),
                patchRestoIn.getJoursOuverture(),
                patchRestoIn.getHoraires(),
                patchRestoIn.getEmail(),
                patchRestoIn.getTelephone(),
                id
        );

        Restaurant restoEntity = restauRepository.getById(id);

        return convertRestoEntityToRestoOut(restoEntity);
    }

    @Override
    public void delete(Integer id) throws NotFoundException {

        try {
            restauRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("No resto found", e);
        }

    }

    private RestoOut convertRestoEntityToRestoOut(Restaurant restaurant) {

        RestoOut restoOut = RestoOut.builder()
                .id_restaurants(restaurant.getId())
                .nom(restaurant.getNom())
                .adress(restaurant.getAdress())
                .nbPlaces(restaurant.getNbPlace())
                .joursOuverture(restaurant.getJoursOuverture())
                .horaires(restaurant.getHoraires())
                .email(restaurant.getEmail())
                .telephone((restaurant.getTelephone()))
                .XIDadmin((restaurant.getXIDadmin()))
                .XIDville((restaurant.getXIDville()))
                .build();
        return restoOut;
    }


    private Restaurant convertRestoInToRestoEntity(CreateRestoIn createRestoIn) {
        Restaurant restaurant = Restaurant.builder()
                .nom(createRestoIn.getNom())
                .adress(createRestoIn.getAdress())
                .nbPlace(createRestoIn.getNbPlace())
                .joursOuverture(createRestoIn.getJoursOuverture())
                .horaires(createRestoIn.getHoraires())
                .email(createRestoIn.getEmail())
                .telephone(createRestoIn.getTelephone())
                .XIDadmin(createRestoIn.getXIDadmin())
                .XIDville(createRestoIn.getXIDville())
                .build();
        return restaurant;
    }

}


