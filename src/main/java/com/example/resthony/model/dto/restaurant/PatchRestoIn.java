package com.example.resthony.model.dto.restaurant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatchRestoIn {

    @NotNull
    public Long id;

    @NotBlank(message = "Nom obligatoire")
    public String name;

    @NotBlank(message = "Adresse obligatoire")
    public String adress;

    @NotNull(message = "Nombre de place obligatoire")
    //@Pattern(regexp = "^[0-9]+",message ="Veuillez entrer un nombre")
    public Integer nb_place;





    @NotBlank(message = "Email obligatoire")
    public String email;

    @NotBlank(message = "Téléphone obligatoire")
    public String telephone;

    @NotNull(message = "Ville obligatoire")
    public Integer ville;

    @Min(value = 1, message="Minimum une heure de rappel")
    @Max(value = 168, message = "Maximum une semaine à l'avance")
    public Long rappel;



}
