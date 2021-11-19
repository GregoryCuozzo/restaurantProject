package com.example.resthony.model.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "pays")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Pays {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pays")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String nom;



}
