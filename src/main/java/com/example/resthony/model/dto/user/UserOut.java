package com.example.resthony.model.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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


}