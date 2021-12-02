package com.example.resthony.repositories;

import com.example.resthony.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * Gestion JPA pour les utilisateurs
 */

@Repository
public interface UserRepository extends JpaRepository<User,Long> {


    @Modifying
    @Transactional
    @Query("update User r set r.username=?1 , r.lastname=?2 , r.firstname=?3, r.email=?4 where r.id =?5")
    int updateUser(String username, String lastname, String firstname, String Email, Long id);

    @Modifying
    @Transactional
    @Query("update User r set r.password=?2 where r.id=?1")
    int updatePass(Long id, String password);

    @Modifying
    @Transactional
    @Query("update User r set r.resto=?1 where r.username=?2")
    int updateResto(Long id, String password);

    public User findByUsername(String username);


    public long  countById(Long id);
}

