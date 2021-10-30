package com.example.resthony.entities;

import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.DateTimeException;

@Entity
@Table(name = "restaurants")
@Data
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_restaurants")
    private int id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String nom;

    @NotNull
    @Column(name = "adress", nullable = false)
    private String adress;

    @NotNull
    @Column(name = "nbPlace", nullable = false)
    private int nbPlace;

    @NotNull
    @Column(name = "joursOuverture", nullable = false)
    private String joursOuverture;

    @NotNull
    @Column(name = "horaires", nullable = false)
    private java.time.LocalTime horaires;

    @Column(name = "email")
    private String email;

    @NotNull
    @Column(name = "telephone", nullable = false)
    private int telephone;

    @NotNull
    @Column(name = "XIDadmin",nullable = false)
    private int XIDadmin;

    @NotNull
    @Column(name = "XIDville",nullable = false)
    private int XIDville;



}
