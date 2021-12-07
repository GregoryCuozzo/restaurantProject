package com.example.resthony.model.dto.visitor;


import com.example.resthony.model.entities.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.sql.Time;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateVisitorIn {

    @NotBlank(message = "Prénom obligatoire")
    public String firstname;

    @NotBlank(message = "Nom obligatoire")
    public String lastname;

    @NotBlank(message = "Email obligatoire")
    public String email;

    @NotBlank(message = "Téléphone obligatoire")
    public String phone;

    public Integer nbcouverts;


    public Date date;


    public Time time;

    public String resto;


}
