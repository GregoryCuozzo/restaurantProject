package com.mycompany.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.mycompany.user.User;


@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    public long countById(Integer id);

}
