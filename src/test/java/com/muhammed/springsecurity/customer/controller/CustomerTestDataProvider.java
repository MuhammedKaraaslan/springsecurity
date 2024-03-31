package com.muhammed.springsecurity.customer.controller;

import org.junit.jupiter.params.provider.Arguments;

import java.util.Random;
import java.util.stream.Stream;

public class CustomerTestDataProvider {

    private static final Random RANDOM = new Random();

    static Stream<Arguments> provideInvalidRegistrationData() {
        return Stream.of(
                Arguments.of("invalidemail", "Password1.", "dummyFirstName", "dummyLastName", "must be a well-formed email address"),
                Arguments.of("", "Password1.", "dummyFirstName", "dummyLastName", "must not be empty"),
                Arguments.of("customer" + RANDOM.nextInt(1000) + "@example.com", "weak", "dummyFirstName", "dummyLastName", "Password must contain at least one lowercase letter, one uppercase letter, one digit, one special character, and be at least 8 characters long"),
                Arguments.of("customer" + RANDOM.nextInt(1000) + "@example.com", "Password1.", "", "dummyLastName", "Name must be at least 2 characters long"),
                Arguments.of("customer" + RANDOM.nextInt(1000) + "@example.com", "Password1.", "dummyFirstName", "", "Name must be at least 2 characters long")
        );
    }
}
