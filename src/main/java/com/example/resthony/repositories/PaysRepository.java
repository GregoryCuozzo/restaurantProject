package com.example.resthony.repositories;


import com.example.resthony.model.entities.Pays;
import com.example.resthony.model.entities.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface PaysRepository extends JpaRepository<Pays, Long> {
    @Modifying
    @Transactional
    @Query("update Pays p set p.nom = ?1 where p.id =?2")
    int updatePays(String Nom,Long id);

//    public long  countById(int id);
}
