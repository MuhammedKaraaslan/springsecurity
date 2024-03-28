package com.muhammed.springsecurity.customer.service.concretes;

import com.muhammed.springsecurity.abstracts.AbstractServiceTest;
import com.muhammed.springsecurity.customer.dataAccess.abstracts.CustomerDao;
import com.muhammed.springsecurity.customer.model.entities.Customer;
import com.muhammed.springsecurity.customer.model.requests.CustomerRegistrationRequest;
import com.muhammed.springsecurity.customer.model.responses.CustomerRegistrationResponse;
import com.muhammed.springsecurity.security.dataAccess.abstracts.TokenDao;
import com.muhammed.springsecurity.security.service.abstracts.JwtService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class CustomerManagerTest extends AbstractServiceTest {

    @Mock
    private CustomerDao customerDao;

    @Mock
    private TokenDao tokenDao;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private CustomerManager underTest;

    // add test for register method
    @Test
    void Given_ValidCustomerRegistrationRequest_When_RegisterIsCalled_Then_ReturnCustomerRegistrationResponse() {
        // Given
        String jwtToken = "jwtToken";
        String refreshToken = "refreshToken";
        CustomerRegistrationRequest customerRegistrationRequest =
                new CustomerRegistrationRequest(
                        "test@email.com",
                        "Password1.",
                        "dummyFirstname",
                        "dummyLastname");

        Customer customer = CustomerRegistrationRequest.toEntity(customerRegistrationRequest);

        CustomerRegistrationResponse expected = new CustomerRegistrationResponse(jwtToken, refreshToken);

        when(passwordEncoder.encode(customer.getPassword())).thenReturn("encodedPassword");
        customer.setPassword("encodedPassword");
        when(jwtService.generateToken(customer)).thenReturn(jwtToken);
        when(jwtService.generateRefreshToken(customer)).thenReturn(refreshToken);
        when(customerDao.save(customer)).thenReturn(customer);
        when(tokenDao.save(any())).thenReturn(null);

        System.out.println("sadsadsa");

        // When
        CustomerRegistrationResponse actual = underTest.register(customerRegistrationRequest);

        // Then
        assertEquals(expected, actual);
    }

}