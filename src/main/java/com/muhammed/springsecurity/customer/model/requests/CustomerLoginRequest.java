package com.muhammed.springsecurity.customer.model.requests;

public record CustomerLoginRequest(
        String email,
        String password
) {
}
