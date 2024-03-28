package com.muhammed.springsecurity.user.dataAccess.abstracts;

import com.muhammed.springsecurity.user.model.entities.User;

import java.util.Optional;

public interface UserDao {

    Optional<User> findByEmail(String email);

    boolean existsUserByEmail(String email);
}
