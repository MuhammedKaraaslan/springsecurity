package com.muhammed.springsecurity.admin.service.concretes;

import com.muhammed.springsecurity.abstracts.AbstractServiceTest;
import com.muhammed.springsecurity.admin.model.entities.Admin;
import com.muhammed.springsecurity.admin.model.requests.AdminLoginRequest;
import com.muhammed.springsecurity.admin.model.requests.AdminRegistrationRequest;
import com.muhammed.springsecurity.admin.model.responses.AdminLoginResponse;
import com.muhammed.springsecurity.admin.model.responses.AdminRegistrationResponse;
import com.muhammed.springsecurity.exceptions.BusinessException;
import com.muhammed.springsecurity.user.business.abstracts.UserService;
import com.muhammed.springsecurity.user.model.responses.UserLoginResponse;
import com.muhammed.springsecurity.user.model.responses.UserRegistrationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class AdminManagerTest extends AbstractServiceTest{

    @Mock
    private UserService userService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private AdminManager underTest;

    @Test
    void Given_ValidCustomerRegistrationRequest_When_RegisterIsCalled_Then_ReturnCustomerRegistrationResponse() {
        // Given
        String jwtToken = "jwtToken";
        String refreshToken = "refreshToken";
        AdminRegistrationRequest adminRegistrationRequest =
                new AdminRegistrationRequest(
                        "test@email.com",
                        "Password1.",
                        "dummyDepartment");

        Admin admin = AdminRegistrationRequest.toEntity(adminRegistrationRequest);
        UserRegistrationResponse userRegistrationResponse = new UserRegistrationResponse(jwtToken, refreshToken);

        AdminRegistrationResponse expected = new AdminRegistrationResponse(jwtToken, refreshToken);

        when(userService.register(admin)).thenReturn(userRegistrationResponse);

        // When
        AdminRegistrationResponse actual = underTest.register(adminRegistrationRequest);

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

        AdminLoginRequest adminLoginRequest =
                new AdminLoginRequest(email, password);

        AdminLoginResponse expected = new AdminLoginResponse(jwtToken, refreshToken);

        when(userService.login(adminLoginRequest.email(), adminLoginRequest.password()))
                .thenReturn(new UserLoginResponse(jwtToken, refreshToken));

        // When
        AdminLoginResponse actual = underTest.login(adminLoginRequest);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void Given_InvalidCustomerLoginRequest_When_LoginIsCalled_Then_ThrowBusinessException() {
        // Given
        AdminLoginRequest invalidRequest =
                new AdminLoginRequest(
                        "invalid@email.com",
                        "WrongPassword");

        when(userService.login(invalidRequest.email(), invalidRequest.password()))
                .thenThrow(new BusinessException("Invalid credentials"));

        // Then
        assertThrows(BusinessException.class, () -> underTest.login(invalidRequest));
    }


}