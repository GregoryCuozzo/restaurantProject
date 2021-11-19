package com.example.resthony.model.dto.villes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VilleOut {

    public Long id;

    public String nom;

    public Integer pays;


}