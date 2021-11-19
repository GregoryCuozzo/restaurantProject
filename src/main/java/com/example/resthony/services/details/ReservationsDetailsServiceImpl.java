package com.example.resthony.services.details;

import com.example.resthony.model.entities.Reservation;
import com.example.resthony.repositories.ReservationRepository;
import com.example.resthony.services.principal.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationsDetailsServiceImpl {
    @Autowired
    private ReservationRepository reservationRepository;

    public ReservationsDetailsServiceImpl(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }


    public void save(Reservation reservation) {
        reservationRepository.save(reservation);
    }


    public List<Reservation> listAll() {
        return reservationRepository.findAll();

    }


    public void delete(Integer id) throws UserNotFoundException {
        Long count = reservationRepository.countById(id);
        reservationRepository.deleteById(id);

    }



}
