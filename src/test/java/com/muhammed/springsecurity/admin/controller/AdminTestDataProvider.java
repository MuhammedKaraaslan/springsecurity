package com.muhammed.springsecurity.admin.controller;

import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public class AdminTestDataProvider {

    static Stream<Arguments> provideInvalidRegistrationData() {
        return Stream.of(
                Arguments.of("invalidemail", "Password1.", "dummyDepartment", "must be a well-formed email address"),
                Arguments.of("", "Password1.", "dummyDepartment", "must not be empty"),
                Arguments.of("test@example.com", "weak", "dummyDepartment", "Password must contain at least one lowercase letter, one uppercase letter, one digit, one special character, and be at least 8 characters long"),
                Arguments.of("test@example.com", "Password1.", "", "Name must be at least 2 characters long")
        );
    }
}
