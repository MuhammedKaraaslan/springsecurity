package com.muhammed.springsecurity.customer.dataAccess.concretes;

import com.muhammed.springsecurity.customer.dataAccess.abstracts.CustomerRepository;
import com.muhammed.springsecurity.customer.model.entities.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CustomerJPADataAccessServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerJPADataAccessService underTest;

    @BeforeEach
    void setUp() {
        reset(customerRepository);
    }

    @Test
    void Given_ValidCustomer_When_SaveIsCalled_Then_ReturnSavedCustomer() {
        //Given
        String email = "test@email.com";
        Customer expected = new Customer(email, "Password1.", "dummyFirstname", "dummyLastname");

        when(customerRepository.save(expected)).thenReturn(expected);

        // Then
        assertEquals(expected, underTest.save(expected));
        verify(customerRepository, times(1)).save(expected);
    }

    @Test
    void Given_CustomerExistsByEmail_When_FindByEmailIsCalled_Then_ReturnCustomer() {
        //Given
        String email = "test@email.com";
        Customer expected = new Customer(email, "Password1.", "dummyFirstname", "dummyLastname");

        when(customerRepository.findByEmail(email)).thenReturn(Optional.of(expected));

        // When
        Optional<Customer> foundCustomer = underTest.findByEmail(email);

        // Then
        assertTrue(foundCustomer.isPresent());
        assertEquals(expected, foundCustomer.get());
    }

    @Test
    void Given_CustomerDoesNotExistByEmail_When_FindByEmailIsCalled_Then_ReturnEmptyOptional() {
        // Given
        String email = "nonexistent@email.com";

        when(customerRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When
        Optional<Customer> foundCustomer = underTest.findByEmail(email);

        // Then
        assertTrue(foundCustomer.isEmpty());
    }

}