package com.example.resthony.model.dto.reservation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Time;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationOut {

    public Long id;

    public Integer client;

    public Date date;

    public Time time;

    public Integer restaurant;

    public Integer user;


}