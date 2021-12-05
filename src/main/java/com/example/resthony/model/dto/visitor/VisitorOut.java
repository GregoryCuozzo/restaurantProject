package com.example.resthony.model.dto.visitor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VisitorOut {

    public Long id;

    public String firstname;

    public String lastname;

    public String email;

    public String phone;

    public String resto;

    public Integer nbcouverts;

    public Date date;

    public Time time;

}
