package com.example.resthony.model.dto.villes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatchVilleIn {

    @NotNull
    public Long id;

    @NotEmpty(message = "ce champ ne peut pas être vide")
    @Size(min = 10, max = 200)
    public String nom;

    @NotEmpty(message = "ce champ ne peut pas être vide")
    @Size(min = 10, max = 200)
    public Integer pays;




}
