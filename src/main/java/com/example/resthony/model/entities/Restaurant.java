package com.example.resthony.model.entities;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "restaurants")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_restaurants")
    public Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    public String nom;

    @NotNull
    @Column(name = "adress", nullable = false)
    public String adress;

    @NotNull
    @Column(name = "nbPlace", nullable = false)
    public int nbPlace;

    @NotNull
    @Column(name = "OpeningDay", nullable = false)
    public String OpeningDay;

    @Column(name = "email")
    public String email;

    @NotNull
    @Column(name = "telephone", nullable = false)
    public Integer telephone;

    // il faut rajouter les innerjoin pour les villes et le restaurateur






}
