package com.muhammed.springsecurity.user.dataAccess.concretes;

import com.muhammed.springsecurity.user.dataAccess.abstracts.UserDao;
import com.muhammed.springsecurity.user.dataAccess.abstracts.UserRepository;
import com.muhammed.springsecurity.user.model.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("user-jpa")
@RequiredArgsConstructor
public class UserJPADataAccessService implements UserDao {

    private final UserRepository userRepository;

    @Override
    public Optional<User> findByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    @Override
    public boolean existsUserByEmail(String email) {
        return this.userRepository.existsUserByEmail(email);
    }

    @Override
    public User save(User user) {
        return this.userRepository.save(user);
    }
}
