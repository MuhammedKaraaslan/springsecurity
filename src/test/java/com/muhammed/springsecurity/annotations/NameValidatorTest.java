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

public class NameValidatorTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void Given_ValidName_When_Validate_Then_NoValidations() {
        // Given
        TestEntity entity = new TestEntity("John Doe");

        // When
        Set<ConstraintViolation<TestEntity>> violations = validator.validate(entity);

        // Then
        assertTrue(violations.isEmpty());
    }

    @ParameterizedTest
    @CsvSource({
            "null",
            "J",
    })
    void Given_InValidName_When_Validate_Then_ValidationsExist(String name) {
        // Given
        TestEntity entity = new TestEntity(name.equals("null") ? null : name);

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
