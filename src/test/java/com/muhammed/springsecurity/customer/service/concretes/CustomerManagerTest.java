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
import com.muhammed.springsecurity.user.business.abstracts.UserService;
import com.muhammed.springsecurity.user.model.responses.UserLoginResponse;
import com.muhammed.springsecurity.user.model.responses.UserRegistrationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CustomerManagerTest extends AbstractServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private CustomerDao customerDao;

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

        when(customerDao.existsCustomerByEmail(customerLoginRequest.email()))
                .thenReturn(true);
        when(userService.login(customerLoginRequest.email(), customerLoginRequest.password()))
                .thenReturn(new UserLoginResponse(jwtToken, refreshToken));

        // When
        CustomerLoginResponse actual = underTest.login(customerLoginRequest);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void Given_NonExistingCustomerEmail_When_LoginIsCalled_Then_ThrowResourceNotFoundException() {
        // Given
        CustomerLoginRequest invalidRequest =
                new CustomerLoginRequest(
                        "nonexisting@email.com",
                        "Password1.");

        when(customerDao.existsCustomerByEmail(invalidRequest.email()))
                .thenReturn(false);

        // Then
        assertThatThrownBy(() -> underTest.login(invalidRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(String.format("Customer not found with email: %s",
                        invalidRequest.email()));
    }

    @Test
    void Given_InvalidCustomerLoginRequest_When_LoginIsCalled_Then_ThrowBusinessException() {
        // Given
        CustomerLoginRequest invalidRequest =
                new CustomerLoginRequest(
                        "test@email.com",
                        "WrongPassword");

        when(customerDao.existsCustomerByEmail(invalidRequest.email()))
                .thenReturn(true);
        when(userService.login(invalidRequest.email(), invalidRequest.password()))
                .thenThrow(new BusinessException("Invalid credentials"));

        // Then
        assertThrows(BusinessException.class, () -> underTest.login(invalidRequest));
    }

    @Test
    void Given_ValidRequest_When_RefreshTokenIsCalled_Then_DelegateToUserService() {
        // When
        underTest.refreshToken(request, response);

        // Then
        verify(userService).refreshToken(request, response);
    }

}