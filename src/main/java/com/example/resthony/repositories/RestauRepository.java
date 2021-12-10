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
    @Query("update Restaurant r set r.name = ?1, r.adress = ?2, r.nb_place = ?3, r.email= ?4, r.telephone = ?5, r.rappel = ?6 where r.id =?7")
   int updateResto(String name, String adress, Integer nb_place, String email, String telephone, Long rappel, Long id);

//    public long  countById(int id);
    public Restaurant findByName(String name);

}

