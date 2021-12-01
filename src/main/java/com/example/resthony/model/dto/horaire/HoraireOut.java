package com.example.resthony.model.dto.horaire;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HoraireOut {

    public Long id;

    public Long restaurant;

    public String jour;

    public String ouverture;

    public String fermeture;
}
