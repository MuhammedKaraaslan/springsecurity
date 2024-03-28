package com.muhammed.springsecurity.customer.dataAccess.abstracts;

import com.muhammed.springsecurity.TestConfig;
import com.muhammed.springsecurity.customer.model.entities.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest // Configures a minimal Spring Boot test environment for JPA components
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // Prevents replacing the test database configuration
@Import({TestConfig.class}) // Imports additional configurations or beans for testing purposes
@Transactional(propagation = Propagation.NOT_SUPPORTED) // Disables transaction management for the test methods
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository underTest;

    @Autowired
    private TestEntityManager entityManager; // Test-specific EntityManager for managing JPA entities in tests

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
    }

    @Test
    void should_ReturnCustomer_When_CustomerExistsByEmail() {
        //Given
        String email = "test@email.com";
        Customer expected = saveCustomer(email, "Password1.", "dummyFirstname", "dummyLastname");

        // When
        Optional<Customer> foundCustomer = underTest.findByEmail(email);

        // Then
        assertTrue(foundCustomer.isPresent());
        assertEquals(expected, foundCustomer.get());
    }

    @Test
    void should_ReturnEmptyOptional_When_CustomerDoesNotExistByEmail() {
        // When
        Optional<Customer> foundCustomer = underTest.findByEmail("nonexistent@email.com");

        // Then
        assertTrue(foundCustomer.isEmpty());
    }

    private Customer saveCustomer(String email, String password, String firstname, String lastname) {
        Customer customer = new Customer(email, password, firstname, lastname);
        entityManager.persist(customer);
        entityManager.flush();
        return customer;
    }

}