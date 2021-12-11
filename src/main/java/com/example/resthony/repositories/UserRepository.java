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
    @Query("update User r set r.username=?1 , r.lastname=?2 , r.firstname=?3, r.email=?4, r.resto =?5, r.phone =?6, r.contact =?7 where r.id =?8")
    int updateUser(String username, String lastname, String firstname, String Email, Integer resto, String phone, String contact, Long id);

    @Modifying
    @Transactional
    @Query("update User r set r.password=?2 where r.id=?1")
    int updatePass(Long id, String password);

    @Modifying
    @Transactional
    @Query("update User r set r.resto=?1 where r.username=?2")
    int updateResto(Long id, String password);

    @Modifying
    @Transactional
    @Query("update User r set r.resetPasswordToken=?1 where r.id=?2")
    int updateToken(String token, Long id);

    public User findByResetPasswordToken(String token);

    public User findByEmail(String email);

    public User findByUsername(String username);




    public long  countById(Long id);
}

