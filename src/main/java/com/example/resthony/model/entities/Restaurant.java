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
    public String name;

    @NotNull
    @Column(name = "adress", nullable = false)
    public String adress;

    @NotNull
    @Column(name = "nb_place", nullable = false)
    public int nb_place;

    @NotNull
    @Column(name = "opening_day", nullable = false)
    public String opening_day;

    @Column(name = "email")
    public String email;

    @NotNull
    @Column(name = "telephone", nullable = false)
    public String telephone;

    @Column (name = "ville")
    public Integer ville;

    @Column (name = "rappel")
    public Long rappel;








}
