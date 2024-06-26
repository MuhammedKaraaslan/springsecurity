package com.muhammed.springsecurity.customer.controller;

import com.muhammed.springsecurity.customer.model.requests.CustomerLoginRequest;
import com.muhammed.springsecurity.customer.model.requests.CustomerRegistrationRequest;
import com.muhammed.springsecurity.customer.model.responses.CustomerLoginResponse;
import com.muhammed.springsecurity.customer.model.responses.CustomerRegistrationResponse;
import com.muhammed.springsecurity.customer.service.abstracts.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CustomerRegistrationResponse> register(
            @Valid @RequestBody CustomerRegistrationRequest customerRegistrationRequest) {

        CustomerRegistrationResponse response = this.customerService.register(customerRegistrationRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .header(
                        HttpHeaders.AUTHORIZATION,
                        response.accessToken(),
                        response.refreshToken()
                )
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<CustomerLoginResponse> login(
            @Valid @RequestBody CustomerLoginRequest customerLoginRequest) {
        CustomerLoginResponse response = customerService.login(customerLoginRequest);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.AUTHORIZATION,
                        response.accessToken()
                )
                .body(response);
    }

    @PostMapping("/refresh-token")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        this.customerService.refreshToken(request, response);
    }
}
