package com.muhammed.springsecurity.user.dataAccess.abstracts;

import com.muhammed.springsecurity.abstracts.AbstractRepositoryTest;
import com.muhammed.springsecurity.model.Role;
import com.muhammed.springsecurity.user.model.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private UserRepository underTest;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
    }

    @Test
    void should_ReturnUser_When_UserExistsByEmail() {
        //Given
        String email = "test@email.com";
        User expected = saveUser(email, "Password1.", Set.of(Role.USER));

        // When
        Optional<User> foundUser = underTest.findByEmail(email);

        // Then
        assertTrue(foundUser.isPresent());
        assertEquals(expected, foundUser.get());
    }

    @Test
    void should_ReturnEmptyOptional_When_UserDoesNotExistByEmail() {
        // When
        Optional<User> foundUser = underTest.findByEmail("nonexistent@email.com");

        // Then
        assertTrue(foundUser.isEmpty());
    }

    @Test
    void should_ReturnTrue_When_UserExistsByEmail() {
        // Given
        String email = "test@email.com";
        saveUser(email, "Password1.", Set.of(Role.USER));

        // Then
        assertTrue(underTest.existsUserByEmail(email));
    }

    @Test
    void should_ReturnFalse_When_UserDoesNotExistByEmail() {
        // When
        String email = "nonexistent@email.com";
        boolean existsUserByEmail = underTest.existsUserByEmail(email);

        // Then
        assertFalse(existsUserByEmail);
    }

    private User saveUser(String email, String password, Set<Role> roles) {
        User user = new User(email, password, roles);
        entityManager.persist(user);
        entityManager.flush();
        return user;
    }

}