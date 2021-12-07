package com.example.resthony.model.entities;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "horaires")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Horaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_horaire")
    public Long id;

    @NotNull
    @Column(name = "restaurant", nullable = false)
    public Long restaurant;

    @NotNull
    @Column(name = "jour", nullable = false)
    public String jour;


    @Column(name = "ouverture", nullable = false)
    public String ouverture;


    @Column(name = "fermeture", nullable = false)
    public String fermeture;
}
