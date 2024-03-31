package com.muhammed.springsecurity.abstracts;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * AbstractTestcontainers class for setting up a PostgreSQL test container for unit testing.
 * Uses Testcontainers library for managing test containers in JUnit 5 tests.
 */
@Testcontainers
@ActiveProfiles("test")
public abstract class AbstractTestcontainers {

    // PostgreSQL test container creation with specific database name, username, and password for unit testing
    @Container
    protected static final PostgreSQLContainer<?> postgreSQLContainer
            = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("user-registration-test")
            .withUsername("test")
            .withPassword("test");

    /**
     * Registers dynamic properties to Spring's DynamicPropertyRegistry for connecting to the PostgreSQL container.
     * Configures Spring datasource properties (url, username, password) with values from the test container.
     */
    @DynamicPropertySource
    private static void registerDataSourceProperties(DynamicPropertyRegistry registry) {
        // Add dynamic properties for datasource URL, username, and password using test container methods
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

//     //Optional: Cleanup method to stop the PostgreSQL container after all tests
//     @AfterAll
//     static void cleanup() {
//         postgreSQLContainer.stop();
//     }
}
