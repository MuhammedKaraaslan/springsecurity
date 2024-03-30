package com.muhammed.springsecurity.annotations;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class NameValidatorTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void Given_ValidName_When_Validate_ThenNoValidations() {
        // Given
        TestEntity entity = new TestEntity("John Doe");

        // When
        Set<ConstraintViolation<TestEntity>> violations = validator.validate(entity);

        // Then
        assertTrue(violations.isEmpty());
    }

    @Test
    void Given_InValidName_When_Validate_ThenValidationsExist() {
        // Given
        TestEntity entity = new TestEntity("J");

        // When
        Set<ConstraintViolation<TestEntity>> violations = validator.validate(entity);

        // Then
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Name must be at least 2 characters long", violations.iterator().next().getMessage());
    }

    private record TestEntity(
            @ValidName
            String name
    ) {
    }
}
