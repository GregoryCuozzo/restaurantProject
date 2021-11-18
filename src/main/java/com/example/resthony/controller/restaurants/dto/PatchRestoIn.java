package com.example.resthony.controller.restaurants.dto;

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
    private Integer id;

    @NotEmpty(message = "Name must not be empty")
    @Size(min = 10, max = 200)
    private String nom;

    @NotNull(message = "Adress must not be empty")
    private String adress;

    @NotNull(message = "Restaurant must have a min. places")
    private Integer nbPlace;

    @NotEmpty(message = "joursOuverture must have days name")
    private String joursOuverture;

    @NotEmpty(message = "horaires must contains hours")
    private java.time.LocalTime horaires;

    @NotEmpty(message = "email must contains the email")
    private String email;

    @NotNull(message = "telephone must have a phone number")
    private Integer telephone;

    @NotNull(message = "Must have an admin")
    private Integer XIDadmin;

    @NotNull(message = "Must have a city")
    private Integer XIDville;
}
