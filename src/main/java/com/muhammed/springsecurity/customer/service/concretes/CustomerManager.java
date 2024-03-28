package com.muhammed.springsecurity.customer.service.concretes;

import com.muhammed.springsecurity.customer.dataAccess.abstracts.CustomerDao;
import com.muhammed.springsecurity.customer.model.entities.Customer;
import com.muhammed.springsecurity.customer.model.requests.CustomerRegistrationRequest;
import com.muhammed.springsecurity.customer.model.responses.CustomerRegistrationResponse;
import com.muhammed.springsecurity.customer.service.abstracts.CustomerService;
import com.muhammed.springsecurity.security.dataAccess.abstracts.TokenDao;
import com.muhammed.springsecurity.security.model.entities.Token;
import com.muhammed.springsecurity.security.model.enums.TokenType;
import com.muhammed.springsecurity.security.service.abstracts.JwtService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerManager implements CustomerService {

    private final CustomerDao customerDao;
    private final TokenDao tokenDao;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public CustomerManager(@Qualifier("customer-jpa") CustomerDao customerDao,
                           @Qualifier("token-jpa") TokenDao tokenDao,
                           JwtService jwtService,
                           PasswordEncoder passwordEncoder) {
        this.customerDao = customerDao;
        this.tokenDao = tokenDao;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
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

    private void saveToken(Customer customer, String accessToken) {
        Token token = Token.builder()
                .user(customer)
                .token(accessToken)
                .tokenType(TokenType.BEARER)
                .build();

        this.tokenDao.save(token);
    }
}
