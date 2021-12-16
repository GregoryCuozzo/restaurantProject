package com.example.resthony.model.dto.visitor;


import com.example.resthony.model.entities.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.util.Date;
import java.sql.Time;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateVisitorIn {

    @NotBlank(message = "Prénom obligatoire")
    public String firstname;

    @NotBlank(message = "Nom obligatoire")
    public String lastname;

    @NotBlank(message = "Email obligatoire")
    public String email;

    @NotBlank(message = "Téléphone obligatoire")
    public String phone;

    @NotNull(message = "Nombre de couvert obligatoire")
    @Min(value = 1, message= "Veuillez réserver pour au moins une personne")
    public Integer nbcouverts;


    @NotNull (message = "Veuillez choisir une date")
    public Date date;

    @NotNull(message = "Veuillez choisir une heure")
    public String time;

    @NotNull(message= "Restaurant obligatoire")
    @Pattern(regexp = "^((?!default).)*$", message = "Veuillez sélectionner une option")
    public String resto;


}
