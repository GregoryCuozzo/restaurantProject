package com.example.resthony.repositories;

import com.example.resthony.model.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.sql.Time;
import java.util.Date;

/**
 * Gestion JPA pour les reservations
 */

@Repository
public interface ReservationRepository extends JpaRepository<Reservation,Long> {

    @Modifying
    @Transactional
    @Query("update Reservation r set r.date = ?1, r.time = ?2, r.restaurant = ?3, r.client = ?4 where r.id =?5")
    int updateReservation(Date date, Time time, Integer restaurant, Integer client, Long id);

     // public long  countById(int id);
}