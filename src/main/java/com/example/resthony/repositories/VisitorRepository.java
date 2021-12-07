package com.example.resthony.repositories;

import com.example.resthony.model.entities.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import javax.transaction.Transactional;
import java.sql.Time;
import java.util.Date;

@Repository
public interface VisitorRepository extends JpaRepository<Visitor,Long> {


    @Modifying
    @Transactional
    @Query("update Visitor r set r.firstname=?1 , r.lastname=?2, r.email=?3, r.phone=?4,r.nbcouverts=?5,r.date=?6, r.time =?7, r.resto = ?8 where r.id =?9")
    int updateVisitor(String firstname, String lastname, String email, String phone, Integer nbcouverts, Date date, Time time, String resto, Long id);
    public long  countById(Long id);
}