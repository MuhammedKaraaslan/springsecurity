package com.muhammed.springsecurity.user.business.concretes;

import com.muhammed.springsecurity.abstracts.AbstractServiceTest;
import com.muhammed.springsecurity.customer.model.entities.Customer;
import com.muhammed.springsecurity.exceptions.BusinessException;
import com.muhammed.springsecurity.exceptions.ResourceNotFoundException;
import com.muhammed.springsecurity.security.model.entities.Token;
import com.muhammed.springsecurity.security.service.abstracts.JwtService;
import com.muhammed.springsecurity.user.dataAccess.abstracts.UserDao;
import com.muhammed.springsecurity.user.model.entities.User;
import com.muhammed.springsecurity.user.model.responses.UserLoginResponse;
import com.muhammed.springsecurity.user.model.responses.UserRegistrationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserManagerTest extends AbstractServiceTest {

    @Mock
    private UserDao userDao;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private UserManager underTest;

    @Test
    void Given_ValidUser_When_RegisterIsCalled_Then_ReturnUserRegistrationResponse() {
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

    @Test
    void Given_ValidEmailAndPassword_When_LoginIsCalled_Then_ReturnUserLoginResponse() {
        // Given
        String jwtToken = "jwtToken";
        String refreshToken = "refreshToken";
        String email = "test@email.com";
        String password = "Password1.";

        User user = new User(
                email,
                password,
                null);
        user.setId(1);

        UserLoginResponse expected = new UserLoginResponse(jwtToken, refreshToken);
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(user, null);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtService.generateToken(user)).thenReturn(jwtToken);
        when(jwtService.generateRefreshToken(user)).thenReturn(refreshToken);
        when(jwtService.save(any())).thenReturn(null);

        // When
        UserLoginResponse actual = underTest.login(email, password);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void Given_ValidEmailAndPasswordAndValidTokens_When_LoginIsCalled_Then_ReturnUserLoginResponse() {
        // Given
        String jwtToken = "jwtToken";
        String refreshToken = "refreshToken";
        String email = "test@email.com";
        String password = "Password1.";

        User user = new User(
                email,
                password,
                null);
        user.setId(1);

        Token token = new Token();
        token.setUser(user);

        UserLoginResponse expected = new UserLoginResponse(jwtToken, refreshToken);

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(user, null);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtService.generateToken(user)).thenReturn(jwtToken);
        when(jwtService.generateRefreshToken(user)).thenReturn(refreshToken);
        when(jwtService.findAllValidTokenByUser(any())).thenReturn(List.of(token));
        when(jwtService.save(any())).thenReturn(null);

        // When
        UserLoginResponse actual = underTest.login(email, password);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void Given_InvalidEmailAndPassword_When_LoginIsCalled_Then_ThrowBusinessException() {
        // Given
        String email = "invalid@email.com";
        String wrongPassword = "WrongPassword";

        when(authenticationManager.authenticate(any()))
                .thenThrow(new BusinessException("Invalid credentials"));

        // Then
        assertThrows(BusinessException.class, () -> underTest.login(email, wrongPassword));

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
        when(userDao.findByEmail(email)).thenReturn(Optional.empty());

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
        when(userDao.findByEmail(customer.getEmail())).thenReturn(Optional.of(customer));
        when(jwtService.isRefreshTokenValid(refreshToken, customer)).thenReturn(false);

        // Then
        assertThatThrownBy(() -> underTest.refreshToken(request, response))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Refresh token is not valid!");
    }

}