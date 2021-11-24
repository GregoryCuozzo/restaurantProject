package com.example.resthony.model.dto.reservation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Date;
import java.sql.Time;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateReservationIn {

    @NotEmpty(message = "ce champ ne peut pas être vide")
    @Size(min = 10, max = 200)
    public Integer client;

    @NotNull(message = "ce champ ne peut pas être vide")
    public Time time;

    @NotNull(message= "ce champ ne peut pas être vide")
    public Integer restaurant;

    @NotEmpty(message= "ce champ ne peut pas être vide")
    public Date date ;



}
