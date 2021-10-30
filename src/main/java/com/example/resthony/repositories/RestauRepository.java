package com.example.resthony.repositories;

import com.example.resthony.entities.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Gestion JPA pour les restaurants
 */
@Repository
public interface RestauRepository {
    public interface UserRepository extends JpaRepository<Restaurant,Integer>{

    }
}
