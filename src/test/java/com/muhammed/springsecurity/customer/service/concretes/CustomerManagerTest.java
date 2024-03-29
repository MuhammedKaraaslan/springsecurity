package com.muhammed.springsecurity.customer.service.concretes;

import com.muhammed.springsecurity.abstracts.AbstractServiceTest;
import com.muhammed.springsecurity.customer.dataAccess.abstracts.CustomerDao;
import com.muhammed.springsecurity.customer.model.entities.Customer;
import com.muhammed.springsecurity.customer.model.requests.CustomerLoginRequest;
import com.muhammed.springsecurity.customer.model.requests.CustomerRegistrationRequest;
import com.muhammed.springsecurity.customer.model.responses.CustomerLoginResponse;
import com.muhammed.springsecurity.customer.model.responses.CustomerRegistrationResponse;
import com.muhammed.springsecurity.exceptions.BusinessException;
import com.muhammed.springsecurity.security.model.entities.Token;
import com.muhammed.springsecurity.security.service.abstracts.JwtService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CustomerManagerTest extends AbstractServiceTest {

    @Mock
    private CustomerDao customerDao;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private CustomerManager underTest;

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

        when(passwordEncoder.encode(customer.getPassword())).thenReturn(customer.getPassword());
        when(jwtService.generateToken(customer)).thenReturn(jwtToken);
        when(jwtService.generateRefreshToken(customer)).thenReturn(refreshToken);
        when(customerDao.save(customer)).thenReturn(customer);
        when(jwtService.save(any())).thenReturn(null);

        // When
        CustomerRegistrationResponse actual = underTest.register(customerRegistrationRequest);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void Given_ValidCustomerLoginRequest_When_LoginIsCalled_Then_ReturnCustomerLoginResponse() {
        // Given
        String jwtToken = "jwtToken";
        String refreshToken = "refreshToken";
        String email = "test@email.com";
        String password = "Password1.";

        CustomerLoginRequest customerLoginRequest =
                new CustomerLoginRequest(email, password);

        Customer customer = new Customer(
                email,
                password,
                "dummyFirstname",
                "dummyLastname");

        CustomerLoginResponse expected = new CustomerLoginResponse(jwtToken, refreshToken);

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(customer, null);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtService.generateToken(customer)).thenReturn(jwtToken);
        when(jwtService.generateRefreshToken(customer)).thenReturn(refreshToken);
        when(jwtService.save(any())).thenReturn(null);

        // When
        CustomerLoginResponse actual = underTest.login(customerLoginRequest);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void Given_ValidCustomerLoginRequestAndValidTokens_When_LoginIsCalled_Then_ReturnCustomerLoginResponse() {
        // Given
        String jwtToken = "jwtToken";
        String refreshToken = "refreshToken";
        String email = "test@email.com";
        String password = "Password1.";

        CustomerLoginRequest customerLoginRequest =
                new CustomerLoginRequest(email, password);

        Customer customer = new Customer(
                email,
                password,
                "dummyFirstname",
                "dummyLastname");

        Token token = new Token();
        token.setUser(customer);

        CustomerLoginResponse expected = new CustomerLoginResponse(jwtToken, refreshToken);

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(customer, null);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtService.generateToken(customer)).thenReturn(jwtToken);
        when(jwtService.generateRefreshToken(customer)).thenReturn(refreshToken);
        when(jwtService.findAllValidTokenByUser(anyInt())).thenReturn(List.of(token));
        when(jwtService.save(any())).thenReturn(null);

        // When
        CustomerLoginResponse actual = underTest.login(customerLoginRequest);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void Given_InvalidCustomerLoginRequest_When_LoginIsCalled_Then_ThrowBusinessException() {
        // Given
        CustomerLoginRequest invalidRequest =
                new CustomerLoginRequest(
                        "invalid@email.com",
                        "WrongPassword");

        when(authenticationManager.authenticate(any()))
                .thenThrow(new BusinessException("Invalid credentials"));

        // Then
        assertThrows(BusinessException.class, () -> underTest.login(invalidRequest));

        // Verify that authentication manager was called once
        verify(authenticationManager, times(1)).authenticate(any());
    }

}