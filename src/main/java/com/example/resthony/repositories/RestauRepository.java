package com.example.resthony.repositories;

import com.example.resthony.model.entities.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;

/**
 * Gestion JPA pour les restaurants
 */
@Repository

public interface RestauRepository extends JpaRepository<Restaurant, Long> {
    @Modifying
    @Transactional
    @Query("update Restaurant r set r.nom = ?1, r.adress = ?2, r.nbPlace = ?3, r.OpeningDay = ?4, r.email= ?5, r.telephone = ?6 where r.id =?7")
   int updateResto(String Nom, String Adress, Integer NbPlace, String OpeningDay, String Email, Integer Telephone, Long id);

//    public long  countById(int id);

}

