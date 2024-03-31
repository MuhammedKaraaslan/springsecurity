package com.muhammed.springsecurity.integration.admin;

import org.junit.jupiter.params.provider.Arguments;

import java.util.Random;
import java.util.stream.Stream;

public class AdminTestDataProvider {

    private static final Random RANDOM = new Random();

    static Stream<Arguments> provideInvalidRegistrationData() {
        return Stream.of(
                Arguments.of("invalidemail", "Password1.", "dummyDepartment", "must be a well-formed email address"),
                Arguments.of("", "Password1.", "dummyDepartment", "must not be empty"),
                Arguments.of("admin" + RANDOM.nextInt(1000) + "@example.com", "weak", "dummyDepartment", "Password must contain at least one lowercase letter, one uppercase letter, one digit, one special character, and be at least 8 characters long"),
                Arguments.of("admin" + RANDOM.nextInt(1000) + "@example.com", "Password1.", "", "Name must be at least 2 characters long")
        );
    }

    public static Stream<Arguments> provideInvalidLoginData() {
        return Stream.of(
                Arguments.of("invalidemail", "Password1.",  "must be a well-formed email address"),
                Arguments.of("admin" + RANDOM.nextInt(1000) + "@example.com", "weak", "Password must contain at least one lowercase letter, one uppercase letter, one digit, one special character, and be at least 8 characters long")
        );
    }
}
