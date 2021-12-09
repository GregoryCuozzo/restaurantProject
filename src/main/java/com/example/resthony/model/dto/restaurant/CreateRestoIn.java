package com.example.resthony.model.dto.restaurant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateRestoIn {

    @NotBlank(message = "Nom obligatoire")
    public String name;

    @NotBlank(message = "Adresse obligatoire")
    public String adress;

    @NotNull(message = "Nombre de place obligatoire")
    @Pattern(regexp = "^[0-9]+",message ="Veuillez entrer un nombre")
    public Integer nb_place;

    @NotBlank(message = "Horaires obligatoires")
    public String opening_day;



    @NotBlank(message = "Email obligatoire")
    public String email;

    @NotBlank(message = "Téléphone obligatoire")
    public String telephone;

    @NotNull(message = "Ville obligatoire")
    public Integer ville;


}
