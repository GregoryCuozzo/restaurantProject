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
public class PatchReservationIn {

    @NotNull
    public Long id;

    @NotEmpty(message = "ce champ ne peut pas être vide")
    @Size(min = 10, max = 200)
    public Date date;

    @NotNull(message = "ce champ ne peut pas être vide")
    public Time time;

    @NotNull(message = "Restaurant must have a min. places")
    public String restaurant;

    @NotEmpty(message = "user must be written")
    public String user;

    @NotNull(message="Le nombre de couverts doit au moins valoir 1")
    public Integer nbcouverts;

    public Integer admin;

}
