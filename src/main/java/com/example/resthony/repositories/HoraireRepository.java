package com.example.resthony.repositories;

import com.example.resthony.model.entities.Horaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;


@Repository
public interface HoraireRepository extends JpaRepository<Horaire, Long> {


    @Modifying
    @Transactional
    @Query("update Horaire h set h.ouverture = ?1, h.fermeture = ?2 where h.id = ?3")
    int updateHoraire (String Ouverture, String Fermeture, Long id);

    public List<Horaire> findHoraireByRestaurant(Long restaurant);
}
