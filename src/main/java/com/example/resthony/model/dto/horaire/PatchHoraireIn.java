package com.example.resthony.model.dto.horaire;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatchHoraireIn {

    @NotNull
    public Long id;

    public String ouverture;

    public String fermeture;
}
