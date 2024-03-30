package com.muhammed.springsecurity.customer.service.concretes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.muhammed.springsecurity.customer.dataAccess.abstracts.CustomerDao;
import com.muhammed.springsecurity.customer.model.entities.Customer;
import com.muhammed.springsecurity.customer.model.requests.CustomerLoginRequest;
import com.muhammed.springsecurity.customer.model.requests.CustomerRegistrationRequest;
import com.muhammed.springsecurity.customer.model.responses.CustomerLoginResponse;
import com.muhammed.springsecurity.customer.model.responses.CustomerRefreshTokenResponse;
import com.muhammed.springsecurity.customer.model.responses.CustomerRegistrationResponse;
import com.muhammed.springsecurity.customer.service.abstracts.CustomerService;
import com.muhammed.springsecurity.exceptions.BusinessException;
import com.muhammed.springsecurity.exceptions.ResourceNotFoundException;
import com.muhammed.springsecurity.security.model.entities.Token;
import com.muhammed.springsecurity.security.model.enums.TokenType;
import com.muhammed.springsecurity.security.service.abstracts.JwtService;
import com.muhammed.springsecurity.user.business.abstracts.UserService;
import com.muhammed.springsecurity.user.model.responses.UserRegistrationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
public class CustomerManager implements CustomerService {

    private final CustomerDao customerDao;
    private final JwtService jwtService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public CustomerManager(@Qualifier("customer-jpa") CustomerDao customerDao,
                           JwtService jwtService,
                           UserService userService,
                           AuthenticationManager authenticationManager) {
        this.customerDao = customerDao;
        this.jwtService = jwtService;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    @Transactional
    public CustomerRegistrationResponse register(CustomerRegistrationRequest customerRegistrationRequest) {
        // Registration logic specific to customers
        // Validate customer-specific fields, etc.
        // Optionally, perform additional tasks specific to customers
        UserRegistrationResponse userRegistrationResponse = userService.register(
                CustomerRegistrationRequest.toEntity(customerRegistrationRequest)
        );

        return new CustomerRegistrationResponse(
                userRegistrationResponse.accessToken(),
                userRegistrationResponse.refreshToken()
        );
    }

    @Override
    public CustomerLoginResponse login(CustomerLoginRequest customerLoginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        customerLoginRequest.email(),
                        customerLoginRequest.password()
                ));

        Customer customer = (Customer) authentication.getPrincipal();

        String jwtToken = jwtService.generateToken(customer);
        String refreshToken = jwtService.generateRefreshToken(customer);

        revokeAllTokens(customer);
        saveToken(customer, jwtToken);

        return new CustomerLoginResponse(jwtToken, refreshToken);
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

        Customer customer = customerDao.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(String.format("User with %s email not found!", email)));

        if (!jwtService.isRefreshTokenValid(refreshToken, customer))
            throw new BusinessException("Refresh token is not valid!");

        String accessToken = jwtService.generateToken(customer);

        revokeAllTokens(customer);
        saveToken(customer, accessToken);

        CustomerRefreshTokenResponse refreshTokenResponse = new CustomerRefreshTokenResponse(accessToken);

        try {
            new ObjectMapper().writeValue(response.getOutputStream(), refreshTokenResponse);
        } catch (IOException e) {
            throw new BusinessException("An error occurred while writing the response!");
        }

    }

    private void saveToken(Customer customer, String accessToken) {
        Token token = Token.builder()
                .user(customer)
                .token(accessToken)
                .tokenType(TokenType.BEARER)
                .build();

        this.jwtService.save(token);
    }

    private void revokeAllTokens(Customer customer) {
        List<Token> validUserTokens = jwtService.findAllValidTokenByUser(customer.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        jwtService.saveAll(validUserTokens);
    }
}
