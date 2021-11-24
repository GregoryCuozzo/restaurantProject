package com.example.resthony.model.dto.restaurant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestoOut {

    public Long id;

    public String nom;

    public String adress;

    public Integer nbPlaces;

    public String openingDay;

    public String email;

    public Integer telephone;

}