package com.muhammed.springsecurity.annotations;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordPatternValidatorTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void Given_ValidPassword_When_Validate_Then_ReturnTrue() {
        // Given
        TestEntity entity = new TestEntity("Test@1234");

        // When
        Set<ConstraintViolation<TestEntity>> violations = validator.validate(entity);

        // Then
        assertTrue(violations.isEmpty());
    }

    @ParameterizedTest
    @CsvSource({
            "test@1234, false",   // Missing uppercase letter
            "TEST@1234, false",   // Missing lowercase letter
            "Test1234, false",    // Missing special character
            "T@1, false",         // Short length
            "t@1, false",         // Short length and missing uppercase letter
            "Test12, false",      // Short length and missing special character
            "Test@, false",       // Short length and missing digit
            "12345678, false",    // Only digits
            "!@#$%^&*, false"     // Only special characters
    })
    void Given_InvalidPasswordFormat_When_Validate_Then_ReturnFalse(String password) {
        // Given
        TestEntity entity = new TestEntity(password);

        // When
        Set<ConstraintViolation<TestEntity>> violations = validator.validate(entity);

        // Then
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Password must contain at least one lowercase letter, one uppercase letter, one digit, one special character, and be at least 8 characters long", violations.iterator().next().getMessage());
    }

    private record TestEntity(
            @PasswordPattern
            String password
    ) {
    }
}