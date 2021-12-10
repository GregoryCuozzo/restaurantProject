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
    @Query("update Restaurant r set r.name = ?1, r.adress = ?2, r.nb_place = ?3, r.opening_day = ?4, r.email= ?5, r.telephone = ?6, r.rappel = ?7 where r.id =?8")
   int updateResto(String name, String adress, Integer nb_place, String opening_day, String email, String telephone, Integer rappel, Long id);

//    public long  countById(int id);
    public Restaurant findByName(String name);

}

