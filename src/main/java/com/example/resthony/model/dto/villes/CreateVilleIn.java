package com.example.resthony.model.dto.villes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateVilleIn {

    @NotEmpty(message = "Veuillez entrer un nom de ville")
    @Pattern(regexp = "^[a-zA-Z]+", message = "Utilisez seulement des lettres dans le nom")
    public String name;

    @NotNull(message = "Veuillez sélectionner un pays")
    public Integer pays;



}
