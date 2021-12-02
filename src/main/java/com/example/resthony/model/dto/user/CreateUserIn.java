package com.example.resthony.model.dto.user;

import com.example.resthony.constants.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserIn {

    @NotEmpty(message = "ce champ ne peut pas être vide")
    @Size(min = 10, max = 200)
    public String username;

    @NotNull(message = "ce champ ne peut pas être vide")
    public String firstname;

    @NotNull(message= "ce champ ne peut pas être vide")
    public String lastname;

    @NotEmpty(message= "ce champ ne peut pas être vide")
    public String email;

    @NotEmpty(message= "ce champ ne peut pas être vide")
    public String password;

    public Collection<RoleEnum> roles;

    public Integer resto;

    public void addRole(RoleEnum role){
        roles.add(role);
    }





}
