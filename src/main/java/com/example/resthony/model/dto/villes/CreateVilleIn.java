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
public class CreateVilleIn {

    @NotEmpty(message = "Name must not be empty")
    @Size(min = 10, max = 200)
    public String nom;

    @NotNull(message = "Adress must not be empty")
    public Integer pays;



}
