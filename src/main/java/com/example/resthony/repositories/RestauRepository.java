package com.example.resthony.repositories;

import com.example.resthony.entities.Restaurant;
import com.example.resthony.entities.User;
import com.example.resthony.services.impl.UserNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Gestion JPA pour les restaurants
 */
@Repository

public interface RestauRepository extends JpaRepository<Restaurant, Integer> {

    Restaurant findByUsername(String name);

    public long  countById(int id);

}

