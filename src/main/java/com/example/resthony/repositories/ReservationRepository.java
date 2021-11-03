package com.example.resthony.repositories;

import com.example.resthony.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Gestion JPA pour les reservations
 */

@Repository
public interface ReservationRepository extends JpaRepository<Reservation,Integer> {

    public long  countById(int id);
}