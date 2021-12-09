package com.example.resthony.model.dto.user;

import com.example.resthony.constants.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.Collection;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserOut {

    public Long id;

    public String username;

    public String firstname;

    public String lastname;

    public String email;

    public String phone;

    public Integer resto;

    public String contact;

    public Collection<RoleEnum> roles;


}