package com.example.resthony.model.dto.visitor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Time;
import java.sql.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatchVisitorIn {

    @NotNull
    public Long id;

    @NotBlank(message = "ce champ ne peut pas être vide")
    public String firstname;

    @NotBlank(message= "ce champ ne peut pas être vide")
    public String lastname;

    @NotBlank(message= "ce champ ne peut pas être vide")
    public String email;

    @NotBlank(message= "ce champ ne peut pas être vide")
    public String phone;


    public Integer nbcouverts;


    public Date date;


    public Time time;

    public String resto;



}
