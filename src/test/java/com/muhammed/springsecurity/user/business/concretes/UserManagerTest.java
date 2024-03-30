package com.muhammed.springsecurity.user.business.concretes;

import com.muhammed.springsecurity.abstracts.AbstractServiceTest;
import com.muhammed.springsecurity.security.model.entities.Token;
import com.muhammed.springsecurity.security.service.abstracts.JwtService;
import com.muhammed.springsecurity.user.business.abstracts.UserService;
import com.muhammed.springsecurity.user.dataAccess.abstracts.UserDao;
import com.muhammed.springsecurity.user.model.entities.User;
import com.muhammed.springsecurity.user.model.responses.UserRegistrationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class UserManagerTest extends AbstractServiceTest {

    @Mock
    private UserDao userDao;

    @Mock
    private PasswordEncoder passwordEncoder;

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
    private UserManager underTest;

    @Test
    void Given_ValidUser_When_Register_Then_ReturnUserRegistrationResponse() {
        //Given

        String jwtToken = "jwtToken";
        String refreshToken = "refreshToken";
        User user = new User("test@emai.com", "password", null);
        UserRegistrationResponse expected = new UserRegistrationResponse(jwtToken, refreshToken);

        when(passwordEncoder.encode(user.getPassword())).thenReturn(user.getPassword());
        when(jwtService.generateToken(user)).thenReturn(jwtToken);
        when(jwtService.generateRefreshToken(user)).thenReturn(refreshToken);
        when(userDao.save(user)).thenReturn(user);

        //When
        UserRegistrationResponse actual = underTest.register(user);

        // Then
        assertEquals(expected, actual);
    }

}