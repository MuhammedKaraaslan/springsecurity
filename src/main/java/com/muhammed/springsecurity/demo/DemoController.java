package com.muhammed.springsecurity.demo;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.muhammed.springsecurity.model.Role.CUSTOMER;


@RestController
@RequestMapping("/api/v1/demo")
public class DemoController {

    @GetMapping("/public-endpoint")
    public String publicEndPoint() {
        return "Hello from public endpoint";
    }

    @GetMapping("/customer-secure-endpoint")
    @PreAuthorize("hasRole(CUSTOMER.name())")
    public String customerSecureEndpoint() {
        return "Hello from customer secure endpoint";
    }

}
