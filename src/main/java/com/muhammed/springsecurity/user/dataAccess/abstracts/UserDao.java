package com.muhammed.springsecurity.user.dataAccess.abstracts;

import com.muhammed.springsecurity.user.model.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    Optional<User> findByEmail(String email);

    boolean existsUserByEmail(String email);

    User save(User user);

    List<User> getAll();
}
