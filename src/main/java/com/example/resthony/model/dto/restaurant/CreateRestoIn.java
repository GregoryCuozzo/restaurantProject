package com.example.resthony.model.dto.restaurant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateRestoIn {

    @NotEmpty(message = "Name must not be empty")
    @Size(min = 10, max = 200)
    public String nom;

    @NotNull(message = "Adress must not be empty")
    public String adress;

    @NotNull(message= "Restaurant must have a min. places")
    public Integer nbPlace;

    @NotEmpty(message= "joursOuverture must have days name")
    public String openingDay;



    @NotEmpty(message = "email must contains the email")
    public String email;

    @NotNull(message = "telephone must have a phone number")
    public Integer telephone;

}
