package com.muhammed.springsecurity.customer.service.concretes;

import com.muhammed.springsecurity.customer.dataAccess.abstracts.CustomerDao;
import com.muhammed.springsecurity.customer.model.entities.Customer;
import com.muhammed.springsecurity.customer.model.requests.CustomerLoginRequest;
import com.muhammed.springsecurity.customer.model.requests.CustomerRegistrationRequest;
import com.muhammed.springsecurity.customer.model.responses.CustomerLoginResponse;
import com.muhammed.springsecurity.customer.model.responses.CustomerRegistrationResponse;
import com.muhammed.springsecurity.customer.service.abstracts.CustomerService;
import com.muhammed.springsecurity.security.dataAccess.abstracts.TokenDao;
import com.muhammed.springsecurity.security.model.entities.Token;
import com.muhammed.springsecurity.security.model.enums.TokenType;
import com.muhammed.springsecurity.security.service.abstracts.JwtService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerManager implements CustomerService {

    private final CustomerDao customerDao;
    private final TokenDao tokenDao;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public CustomerManager(@Qualifier("customer-jpa") CustomerDao customerDao,
                           @Qualifier("token-jpa") TokenDao tokenDao,
                           JwtService jwtService,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager) {
        this.customerDao = customerDao;
        this.tokenDao = tokenDao;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @Override
    @Transactional
    public CustomerRegistrationResponse register(CustomerRegistrationRequest customerRegistrationRequest) {

        Customer customer = CustomerRegistrationRequest.toEntity(customerRegistrationRequest);
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));

        String accessToken = jwtService.generateToken(customer);
        String refreshToken = jwtService.generateRefreshToken(customer);

        Customer savedCustomer = customerDao.save(customer);

        saveToken(savedCustomer, accessToken);

        return new CustomerRegistrationResponse(accessToken, refreshToken);
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

    private void saveToken(Customer customer, String accessToken) {
        Token token = Token.builder()
                .user(customer)
                .token(accessToken)
                .tokenType(TokenType.BEARER)
                .build();

        this.tokenDao.save(token);
    }

    private void revokeAllTokens(Customer customer) {
        List<Token> validUserTokens = tokenDao.findAllValidTokenByUser(customer.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenDao.saveAll(validUserTokens);
    }
}
