package com.example.resthony.services.details;

import com.example.resthony.model.dto.restaurant.CreateRestoIn;
import com.example.resthony.model.dto.restaurant.PatchRestoIn;
import com.example.resthony.model.dto.restaurant.RestoOut;
import com.example.resthony.model.entities.Restaurant;
import com.example.resthony.repositories.ReservationRepository;
import com.example.resthony.repositories.RestauRepository;
import com.example.resthony.services.principal.RestoService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RestauDetailsServiceImpl implements RestoService {
    private final RestauRepository restauRepository;
    private final ReservationRepository reservationRepository;

    @Autowired
    public RestauDetailsServiceImpl(RestauRepository restauRepository, ReservationRepository reservationRepository) {
        this.restauRepository = restauRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public RestoOut get(Long id) {
        Restaurant restaurant = restauRepository.findById(id).orElse(null);
        if (restaurant == null) return null;
        RestoOut restoOut = convertRestoEntityToRestoOut(restaurant);
        return restoOut;
    }


    @Override
    public List<RestoOut> getAll() {
        List<Restaurant> restoEntities = restauRepository.findAll();
        List<RestoOut> restoOuts = new ArrayList<>();
        for (Restaurant restaurant : restoEntities) {
            restoOuts.add(convertRestoEntityToRestoOut(restaurant));
        }
        return restoOuts;
    }

    @Override
    public RestoOut findByName(String name) {
        Restaurant restaurant = restauRepository.findByName(name);
        if (restaurant == null) return null;
        RestoOut restoOut = convertRestoEntityToRestoOut(restaurant);
        return restoOut;
    }

    @Override
    public RestoOut create(CreateRestoIn createRestoIn) {
        Restaurant restaurant = convertRestoInToRestoEntity(createRestoIn);
        Restaurant newRestaurant = restauRepository.save(restaurant);

        return convertRestoEntityToRestoOut(newRestaurant);
    }


    @Override
    public RestoOut patch(Long id, PatchRestoIn patchRestoIn) {

        restauRepository.updateResto(
                patchRestoIn.getName(),
                patchRestoIn.getAdress(),
                patchRestoIn.getNb_place(),
                patchRestoIn.getEmail(),
                patchRestoIn.getTelephone(),
                patchRestoIn.getRappel(),
                id
        );

        Restaurant restoEntity = restauRepository.getById(id);

        return convertRestoEntityToRestoOut(restoEntity);
    }

    @Override
    public void delete(Long id) throws NotFoundException {

        try {
            restauRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("No resto found", e);
        }

    }

    private RestoOut convertRestoEntityToRestoOut(Restaurant restaurant) {

        RestoOut restoOut = RestoOut.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .adress(restaurant.getAdress())
                .nb_place(restaurant.getNb_place())
                .email(restaurant.getEmail())
                .telephone((restaurant.getTelephone()))
                .rappel((restaurant.getRappel()))

                .build();
        return restoOut;
    }


    private Restaurant convertRestoInToRestoEntity(CreateRestoIn createRestoIn) {
        Restaurant restaurant = Restaurant.builder()
                .name(createRestoIn.getName())
                .adress(createRestoIn.getAdress())
                .nb_place(createRestoIn.getNb_place())
                .email(createRestoIn.getEmail())
                .telephone(createRestoIn.getTelephone())
                .rappel(createRestoIn.getRappel())
                .build();
        return restaurant;
    }

}


