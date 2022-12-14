package com.example.resthony.model.dto.reservation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.sql.Time;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationOut {

    public Long id;

    public String user;

    public Date date;

    public String time;

    public String restaurant;

    public Integer admin;

    public Integer nbcouverts;


}