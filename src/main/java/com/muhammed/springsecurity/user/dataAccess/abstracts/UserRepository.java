package com.muhammed.springsecurity.user.dataAccess.abstracts;

import com.muhammed.springsecurity.user.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    boolean existsUserByEmail(String email);

}
