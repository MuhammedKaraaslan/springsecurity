package com.muhammed.springsecurity.abstracts;

import com.muhammed.springsecurity.TestConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest // Configures a minimal Spring Boot test environment for JPA components
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // Prevents replacing the test database configuration
@Import({TestConfig.class}) // Imports additional configurations or beans for testing purposes
@Transactional // Enables transaction management for the test methods
public abstract class AbstractRepositoryTest {

    @Autowired
    protected TestEntityManager entityManager; // Test-specific EntityManager for managing JPA entities in tests

}
