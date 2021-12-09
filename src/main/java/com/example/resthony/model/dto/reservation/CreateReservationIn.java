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
public class CreateReservationIn {

    @NotNull(message = "Nom d'utilisateur obligatoire")
    public String user;

    @NotNull(message = "Heure obligatoire")
    public Time time;

    @NotNull(message = "Restaurant obligatoire")
    @Pattern(regexp = "^((?!default).)*$", message = "Veuillez sélectionner une option")
    public String restaurant;

    @NotNull(message = "Date obligatoire")
    public Date date ;

    @NotNull(message = "Nombre de couverts obligatoire")
    public Integer nbcouverts;

    public Integer admin ;


}
