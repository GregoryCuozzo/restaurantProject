package com.example.resthony.services.principal;

import com.example.resthony.model.dto.reservation.CreateReservationIn;
import com.example.resthony.model.dto.reservation.PatchReservationIn;
import com.example.resthony.model.dto.reservation.ReservationOut;


import com.example.resthony.model.entities.User;
import javassist.NotFoundException;

import java.util.List;

public interface ReservationService {

    ReservationOut get(Long id);
    ReservationOut create(CreateReservationIn createReservationIn);
    ReservationOut patch(Long id, PatchReservationIn patchReservationIn);
    void delete(Long id) throws NotFoundException;
    List<ReservationOut> getAll();
    List<ReservationOut> findByUser(User user);
}
