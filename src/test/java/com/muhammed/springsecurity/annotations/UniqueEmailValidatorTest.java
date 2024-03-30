package com.muhammed.springsecurity.annotations;

import com.muhammed.springsecurity.user.dataAccess.abstracts.UserDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UniqueEmailValidatorTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UniqueEmailValidator underTest;

    @Test
    void Given_ExistingEmail_When_Validate_Then_NoViolations() {
        // Given
        String email = "existing@example.com";

        when(userDao.existsUserByEmail(email)).thenReturn(false);

        // Then
        assertTrue(underTest.isValid(email, null));
    }

    @Test
    void Given_NonExistingEmail_When_Validate_Then_ViolationsExist() {
        // Given
        String email = "nonexisting@example.com";

        when(userDao.existsUserByEmail(email)).thenReturn(true);

        // Then
        assertFalse(underTest.isValid(email, null));
    }
}
