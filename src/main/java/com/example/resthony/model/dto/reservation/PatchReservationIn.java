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

    @NotBlank
    public Long id;

    @NotBlank(message = "Date obligatoire")
    public Date date;

    @NotBlank(message = "Heure obligatoire")
    public Time time;

    @NotBlank(message = "Restaurant obligatoire")
    public String restaurant;

    @NotBlank(message = "Utilisateur obligatoire")
    public String user;

    @NotBlank(message="Nombre de couvert obligatoire")
    @Min(value = 1, message = "Il doit y avoir au moins un couvert" )
    public Integer nbcouverts;

    public Integer admin;

}
