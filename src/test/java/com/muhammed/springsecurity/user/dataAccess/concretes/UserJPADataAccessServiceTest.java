package com.muhammed.springsecurity.user.dataAccess.concretes;

import com.muhammed.springsecurity.model.Role;
import com.muhammed.springsecurity.user.dataAccess.abstracts.UserRepository;
import com.muhammed.springsecurity.user.model.entities.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserJPADataAccessServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserJPADataAccessService underTest;

    @AfterEach
    void tearDown() {
        reset(userRepository);
    }

    @Test
    void should_ReturnUser_When_UserExistsByEmail() {
        // Given
        String email = "test@email.com";
        User expected = new User(email, "Password1.", Set.of(Role.USER));

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(expected));

        // When
        Optional<User> foundUser = underTest.findByEmail(email);

        // Then
        assertTrue(foundUser.isPresent());
        assertEquals(expected, foundUser.get());
    }

    @ParameterizedTest
    @CsvSource({
            "user1@email.com, true",
            "user2@email.com, false"
    })
    void should_ReturnCorrectUserExistence(String email, boolean exists) {
        // Given
        when(userRepository.findByEmail(email))
                .thenReturn(exists
                        ? Optional.of(new User(email, "Password", Set.of(Role.USER)))
                        : Optional.empty()
                );

        // When
        Optional<User> foundUser = underTest.findByEmail(email);

        // Then
        assertEquals(exists, foundUser.isPresent());
    }
}