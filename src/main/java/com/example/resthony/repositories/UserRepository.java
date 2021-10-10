package com.example.resthony.repositories;

import com.example.resthony.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Gestion JPA pour les utilisateurs
 */

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    /**
     * Définit une fonction qui permet de récupérer un utilisateur sur base du username
     * @param username
     * @return
     */
    User findByUsername(String username);

    public long  countById(int id);
}

