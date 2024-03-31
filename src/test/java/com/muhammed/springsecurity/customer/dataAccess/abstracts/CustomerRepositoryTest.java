package com.muhammed.springsecurity.customer.dataAccess.abstracts;

import com.muhammed.springsecurity.abstracts.AbstractRepositoryTest;
import com.muhammed.springsecurity.customer.model.entities.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CustomerRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private CustomerRepository underTest;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
    }

    @Test
    void Given_CustomerExistsByEmail_When_FindByEmailIsCalled_Then_ReturnCustomer() {
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
    void Given_CustomerDoesNotExistByEmail_When_FindByEmailIsCalled_Then_ReturnEmptyOptional() {
        // When
        Optional<Customer> foundCustomer = underTest.findByEmail("nonexistent@email.com");

        // Then
        assertTrue(foundCustomer.isEmpty());
    }

    @Test
    void Given_CustomerExistsByEmail_When_ExistsCustomerByEmail_Then_ReturnTrue() {
        //Given
        String email = "test@email.com";
        saveCustomer(email, "Password1.", "dummyFirstname", "dummyLastname");

        // Then
        assertTrue(underTest.existsCustomerByEmail(email));
    }

    @Test
    void Given_CustomerDoesNotExistByEmail_When_ExistsCustomerByEmail_Then_ReturnFalse() {
        //Given
        String email = "test@email.com";

        // Then
        assertFalse(underTest.existsCustomerByEmail(email));
    }

    private Customer saveCustomer(String email, String password, String firstname, String lastname) {
        Customer customer = new Customer(email, password, firstname, lastname);
        entityManager.persist(customer);
        entityManager.flush();
        return customer;
    }

}