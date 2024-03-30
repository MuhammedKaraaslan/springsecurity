package com.muhammed.springsecurity.customer.service.concretes;

import com.muhammed.springsecurity.abstracts.AbstractServiceTest;
import com.muhammed.springsecurity.customer.dataAccess.abstracts.CustomerDao;
import com.muhammed.springsecurity.customer.model.entities.Customer;
import com.muhammed.springsecurity.customer.model.requests.CustomerLoginRequest;
import com.muhammed.springsecurity.customer.model.requests.CustomerRegistrationRequest;
import com.muhammed.springsecurity.customer.model.responses.CustomerLoginResponse;
import com.muhammed.springsecurity.customer.model.responses.CustomerRegistrationResponse;
import com.muhammed.springsecurity.exceptions.BusinessException;
import com.muhammed.springsecurity.exceptions.ResourceNotFoundException;
import com.muhammed.springsecurity.security.model.entities.Token;
import com.muhammed.springsecurity.security.service.abstracts.JwtService;
import com.muhammed.springsecurity.user.business.abstracts.UserService;
import com.muhammed.springsecurity.user.model.responses.UserLoginResponse;
import com.muhammed.springsecurity.user.model.responses.UserRegistrationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CustomerManagerTest extends AbstractServiceTest {

    @Mock
    private CustomerDao customerDao;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

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
        UserRegistrationResponse userRegistrationResponse = new UserRegistrationResponse(jwtToken, refreshToken);

        CustomerRegistrationResponse expected = new CustomerRegistrationResponse(jwtToken, refreshToken);

        when(userService.register(customer)).thenReturn(userRegistrationResponse);

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

        CustomerLoginResponse expected = new CustomerLoginResponse(jwtToken, refreshToken);

        when(userService.login(customerLoginRequest.email(), customerLoginRequest.password()))
                .thenReturn(new UserLoginResponse(jwtToken, refreshToken));

        // When
        CustomerLoginResponse actual = underTest.login(customerLoginRequest);

        // Then
        assertEquals(expected, actual);
    }

    @Ignore
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
        when(jwtService.findAllValidTokenByUser(any())).thenReturn(List.of(token));
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

    @ParameterizedTest
    @CsvSource({"null", "InvalidToken "})
    void Given_MissingOrInvalidAuthHeader_When_RefreshTokenIsCalled_Then_ThrowBusinessException(String authHeader) {
        // Given
        when(request.getHeader("Authorization")).thenReturn(
                authHeader.equals("null") ? null : authHeader
        );

        // Then
        assertThatThrownBy(() -> underTest.refreshToken(request, response))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Authorization header is missing or invalid!");
    }

    @Test
    void Given_NullEmail_When_RefreshTokenIsCalled_Then_ThrowBusinessException() {
        // Given
        when(request.getHeader("Authorization")).thenReturn("Bearer refreshToken");
        when(jwtService.extractUsername("refreshToken")).thenReturn(null);

        // Then
        assertThatThrownBy(() -> underTest.refreshToken(request, response))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Email cannot be extracted from refresh token!");
    }

    @Test
    void Given_NonexistentEmail_When_FindByEmailIsCalled_Then_ThrowResourceNotFoundException() {
        // Given
        String email = "nonexistent@test.com";
        when(request.getHeader("Authorization")).thenReturn("Bearer refreshToken");
        when(jwtService.extractUsername("refreshToken")).thenReturn(email);
        when(customerDao.findByEmail(email)).thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> underTest.refreshToken(request, response))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(String.format("User with %s email not found!", email));
    }

    @Test
    void Given_InvalidRefreshToken_When_RefreshTokenIsCalled_Then_ThrowBusinessException() {
        // Given
        String refreshToken = "invalidRefreshToken";
        Customer customer = new Customer("test@email.com", "password", "dummyFirstname", "dummyLastname");
        when(request.getHeader("Authorization")).thenReturn("Bearer refreshToken");
        when(jwtService.extractUsername("refreshToken")).thenReturn(customer.getEmail());
        when(customerDao.findByEmail(customer.getEmail())).thenReturn(Optional.of(customer));
        when(jwtService.isRefreshTokenValid(refreshToken, customer)).thenReturn(false);

        // Then
        assertThatThrownBy(() -> underTest.refreshToken(request, response))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Refresh token is not valid!");
    }


}