package com.example.resthony.model.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.sql.Time;


@Entity
@Table(name = "reservations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reservation")
    private Long id;

    @NotNull
    @Column(name = "date", nullable = false, unique = true)
    private Date date;

    @NotNull
    @Column(name = "time", nullable = false, unique = true)
    private Time time;

    @NotNull
    @Column(name = "restaurant", nullable = false, unique = true)
    private Integer restaurant;

    @NotNull
    @Column(name = "client", nullable = false, unique = true)
    private Integer client;
}
