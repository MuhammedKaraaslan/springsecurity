package com.muhammed.springsecurity.customer.controller;

import com.muhammed.springsecurity.customer.model.requests.CustomerRegistrationRequest;
import com.muhammed.springsecurity.customer.model.responses.CustomerRegistrationResponse;
import com.muhammed.springsecurity.customer.service.abstracts.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
                .build();
    }

}
