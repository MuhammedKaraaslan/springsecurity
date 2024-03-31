package com.muhammed.springsecurity.customer.service.concretes;

import com.muhammed.springsecurity.customer.dataAccess.abstracts.CustomerDao;
import com.muhammed.springsecurity.customer.model.requests.CustomerLoginRequest;
import com.muhammed.springsecurity.customer.model.requests.CustomerRegistrationRequest;
import com.muhammed.springsecurity.customer.model.responses.CustomerLoginResponse;
import com.muhammed.springsecurity.customer.model.responses.CustomerRegistrationResponse;
import com.muhammed.springsecurity.customer.service.abstracts.CustomerService;
import com.muhammed.springsecurity.exceptions.ResourceNotFoundException;
import com.muhammed.springsecurity.user.business.abstracts.UserService;
import com.muhammed.springsecurity.user.model.responses.UserLoginResponse;
import com.muhammed.springsecurity.user.model.responses.UserRegistrationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerManager implements CustomerService {

    private final CustomerDao customerDao;
    private final UserService userService;

    public CustomerManager(@Qualifier("customer-jpa") CustomerDao customerDao,
                           UserService userService) {
        this.customerDao = customerDao;
        this.userService = userService;
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
        // Login logic specific to customers
        // Validate customer-specific fields, etc.
        // Optionally, perform additional tasks specific to customers

        if (!customerDao.existsCustomerByEmail(customerLoginRequest.email())) {
            throw new ResourceNotFoundException(String.format("Customer not found with email: %s",
                    customerLoginRequest.email()));
        }

        UserLoginResponse userLoginResponse = userService.login(
                customerLoginRequest.email(),
                customerLoginRequest.password()
        );

        return new CustomerLoginResponse(
                userLoginResponse.accessToken(),
                userLoginResponse.refreshToken()
        );
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        this.userService.refreshToken(request, response);
    }
}
