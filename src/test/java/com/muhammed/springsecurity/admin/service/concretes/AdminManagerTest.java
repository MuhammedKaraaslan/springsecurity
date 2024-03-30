package com.muhammed.springsecurity.admin.service.concretes;

import com.muhammed.springsecurity.abstracts.AbstractServiceTest;
import com.muhammed.springsecurity.admin.model.entities.Admin;
import com.muhammed.springsecurity.admin.model.requests.AdminRegistrationRequest;
import com.muhammed.springsecurity.admin.model.responses.AdminRegistrationResponse;
import com.muhammed.springsecurity.customer.model.entities.Customer;
import com.muhammed.springsecurity.customer.model.requests.CustomerRegistrationRequest;
import com.muhammed.springsecurity.customer.model.responses.CustomerRegistrationResponse;
import com.muhammed.springsecurity.user.business.abstracts.UserService;
import com.muhammed.springsecurity.user.model.responses.UserRegistrationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
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


}