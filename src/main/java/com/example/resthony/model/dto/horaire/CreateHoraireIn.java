package com.example.resthony.model.dto.horaire;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.validation.constraints.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateHoraireIn {

    @NotNull(message = "ce champ ne peut pas être vide")
    public Long restaurant;

    @NotEmpty(message = "ce champ ne peut pas être vide")
    public String jour;

    public String ouverture;

    public String fermeture;
}
