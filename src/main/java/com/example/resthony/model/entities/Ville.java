package com.example.resthony.model.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "villes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Ville {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ville")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String nom;

    @NotNull
    @Column(name = "pays", nullable = false)
    private Integer pays;



}
