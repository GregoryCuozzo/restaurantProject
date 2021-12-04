package com.example.resthony.model.dto.visitor;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateVisitorIn {

    @NotNull(message = "ce champ ne peut pas être vide")
    public String firstname;

    @NotNull(message= "ce champ ne peut pas être vide")
    public String lastname;

    @NotEmpty(message= "ce champ ne peut pas être vide")
    public String email;

    @NotEmpty(message= "ce champ ne peut pas être vide")
    public String phone;

    public Integer resto;

}
