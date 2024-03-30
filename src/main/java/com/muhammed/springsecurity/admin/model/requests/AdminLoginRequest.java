package com.muhammed.springsecurity.admin.model.requests;

public record AdminLoginRequest(
        String email,
        String password
) {
}
