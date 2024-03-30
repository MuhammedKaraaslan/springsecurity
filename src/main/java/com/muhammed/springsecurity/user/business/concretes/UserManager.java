package com.muhammed.springsecurity.user.business.concretes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.muhammed.springsecurity.customer.model.entities.Customer;
import com.muhammed.springsecurity.customer.model.responses.CustomerRefreshTokenResponse;
import com.muhammed.springsecurity.exceptions.BusinessException;
import com.muhammed.springsecurity.exceptions.ResourceNotFoundException;
import com.muhammed.springsecurity.security.model.entities.Token;
import com.muhammed.springsecurity.security.model.enums.TokenType;
import com.muhammed.springsecurity.security.service.abstracts.JwtService;
import com.muhammed.springsecurity.user.business.abstracts.UserService;
import com.muhammed.springsecurity.user.dataAccess.abstracts.UserDao;
import com.muhammed.springsecurity.user.model.entities.User;
import com.muhammed.springsecurity.user.model.responses.UserLoginResponse;
import com.muhammed.springsecurity.user.model.responses.UserRegistrationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
public class UserManager implements UserService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public UserManager(@Qualifier("user-jpa") UserDao userDao,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager
    ) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    @Transactional
    public UserRegistrationResponse register(User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        User savedUser = userDao.save(user);

        saveToken(savedUser, jwtToken);

        return new UserRegistrationResponse(jwtToken, refreshToken);
    }

    @Override
    public UserLoginResponse login(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));

        User user = (User) authentication.getPrincipal();

        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        revokeAllTokens(user.getId());
        saveToken(user, jwtToken);

        return new UserLoginResponse(jwtToken, refreshToken);
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String headerKeyword = "Bearer ";

        if (authHeader == null || !authHeader.startsWith(headerKeyword))
            throw new BusinessException("Authorization header is missing or invalid!");

        String refreshToken = authHeader.substring(headerKeyword.length());
        String email = jwtService.extractUsername(refreshToken);

        if (email == null)
            throw new BusinessException("Email cannot be extracted from refresh token!");

        User user = userDao.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(String.format("User with %s email not found!", email)));

        if (!jwtService.isRefreshTokenValid(refreshToken, user))
            throw new BusinessException("Refresh token is not valid!");

        String accessToken = jwtService.generateToken(user);

        revokeAllTokens(user.getId());
        saveToken(user, accessToken);

        CustomerRefreshTokenResponse refreshTokenResponse = new CustomerRefreshTokenResponse(accessToken);

        try {
            new ObjectMapper().writeValue(response.getOutputStream(), refreshTokenResponse);
        } catch (IOException e) {
            throw new BusinessException("An error occurred while writing the response!");
        }

    }

    private void saveToken(User user, String accessToken) {
        Token token = Token.builder()
                .user(user)
                .token(accessToken)
                .tokenType(TokenType.BEARER)
                .build();

        this.jwtService.save(token);
    }

    private void revokeAllTokens(int userId) {
        List<Token> validUserTokens = jwtService.findAllValidTokenByUser(userId);
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        jwtService.saveAll(validUserTokens);
    }
}
