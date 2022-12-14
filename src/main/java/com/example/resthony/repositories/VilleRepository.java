package com.example.resthony.repositories;

import com.example.resthony.model.entities.Ville;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
@Repository

public interface VilleRepository extends JpaRepository<Ville, Long> {

    @Modifying
    @Transactional
    @Query("update Ville v set v.name = ?1, v.pays = ?2 where v.id =?3")
    int updateVille(String Name,Integer pays,Long id);

//    public long  countById(int id);
}
