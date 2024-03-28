package com.muhammed.springsecurity.customer.service.abstracts;

import com.muhammed.springsecurity.customer.model.requests.CustomerLoginRequest;
import com.muhammed.springsecurity.customer.model.requests.CustomerRegistrationRequest;
import com.muhammed.springsecurity.customer.model.responses.CustomerLoginResponse;
import com.muhammed.springsecurity.customer.model.responses.CustomerRegistrationResponse;

public interface CustomerService {

    CustomerRegistrationResponse register(CustomerRegistrationRequest customerRegistrationRequest);

    CustomerLoginResponse login(CustomerLoginRequest customerLoginRequest);
}
