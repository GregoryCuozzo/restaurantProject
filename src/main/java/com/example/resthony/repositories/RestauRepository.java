package com.example.resthony.repositories;

import com.example.resthony.entities.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;

/**
 * Gestion JPA pour les restaurants
 */
@Repository

public interface RestauRepository extends JpaRepository<Restaurant, Integer> {
    @Modifying
    @Transactional
    @Query("update Restaurant r set r.nom = ?1, r.adress = ?2, r.nbPlace = ?3, r.joursOuverture = ?4, r.horaires = ?5, r.email= ?6, r.telephone = ?7 where r.id =?8")
    int updateResto(String nom, String adress, Integer nbPlace, String joursOuverture, java.time.LocalTime horaires, String email, Integer telephone, Integer id);

//    public long  countById(int id);

}

