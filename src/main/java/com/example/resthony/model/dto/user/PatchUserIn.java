package com.example.resthony.model.dto.user;

import com.example.resthony.constants.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.util.Collection;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatchUserIn {

    @NotNull
    public Long id;

    @NotBlank(message = "Pseudo obligatoire")
    @Size(min = 4, max = 200, message ="Le pseudo doit faire au moins 4 caractères")
    @Pattern(regexp = "^[A-Za-z0-9]+", message = "Utilisez seulement des lettres et des chiffres dans votre pseudo")
    public String username;


    @NotBlank(message = "Prénom obligatoire")
    @Pattern(regexp = "^[a-zA-Z]+", message = "Utilisez seulement des lettres dans votre prénom")
    public String firstname;

    @NotBlank(message= "Nom obligatoire")
    @Pattern(regexp = "^[a-zA-Z]+", message = "Utilisez seulement des lettres dans votre nom")
    public String lastname;

    @NotBlank(message= "Email obligatoire")
    @Email(message = "Veuillez rentrer un email valide")
    public String email;

    public String phone;

    public Collection<RoleEnum> roles;

    public Integer resto;

    public String contact;



}
