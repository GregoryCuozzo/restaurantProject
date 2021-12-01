package com.example.resthony.model.dto.user;

import com.example.resthony.constants.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatchUserIn {

    @NotNull
    public Long id;

    @NotEmpty(message = "ce champ ne peut pas être vide")
    @Size(min = 10, max = 200)
    public String username;

    @NotNull(message = "ce champ ne peut pas être vide")
    public String lastname;

    @NotNull(message = "ce champ ne peut pas être vide")
    public String firstname;


    @NotEmpty(message = "ce champ ne peut pas être vide")
    public String email;

    public Collection<RoleEnum> roles;

    public Integer resto;



}
