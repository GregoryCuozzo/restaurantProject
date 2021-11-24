package com.example.resthony.model.dto.restaurant;

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
public class PatchRestoIn {

    @NotNull
    public Long id;

    @NotEmpty(message = "ce champ ne peut pas être vide")
    @Size(min = 10, max = 200)
    public String nom;

    @NotNull(message = "ce champ ne peut pas être vide")
    public String adress;

    @NotNull(message = "ce champ ne peut pas être vide")
    public Integer nbPlace;

    @NotEmpty(message = "ce champ ne peut pas être vide")
    public String openingDay;



    @NotEmpty(message = "ce champ ne peut pas être vide")
    public String email;

    @NotNull(message = "ce champ ne peut pas être vide")
    public Integer telephone;



}