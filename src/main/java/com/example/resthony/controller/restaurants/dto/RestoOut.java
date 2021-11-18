package com.example.resthony.controller.restaurants.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestoOut {

    private Integer id_restaurants;

    private String nom;

    private String adress;

    private Integer nbPlaces;

    private String joursOuverture;

    private java.time.LocalTime horaires;

    private String email;

    private Integer telephone;

    private Integer XIDadmin;

    private Integer XIDville;
}