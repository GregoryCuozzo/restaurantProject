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

    public String name;

    public String adress;

    public Integer nb_place;

    public String opening_day;

    public String email;

    public String telephone;

    public Integer ville;

    public Long rappel;

}