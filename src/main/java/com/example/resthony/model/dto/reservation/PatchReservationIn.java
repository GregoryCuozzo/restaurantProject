package com.example.resthony.model.dto.reservation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.sql.Date;
import java.sql.Time;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatchReservationIn {

    @NotNull
    public Long id;

    @NotNull(message = "Date obligatoire")
    public Date date;

    @NotNull(message = "Heure obligatoire")
    public String time;

    @NotNull(message = "Restaurant obligatoire")
    @Pattern(regexp = "^((?!default).)*$", message = "Veuillez sélectionner une option")
    public String restaurant;

    @NotNull(message = "Utilisateur obligatoire")
    public String user;

    @NotNull(message="Nombre de couvert obligatoire")
    @Min(value = 1, message = "Veuillez réserver pour au moins une personne" )
    public Integer nbcouverts;

    public Integer admin;

}
