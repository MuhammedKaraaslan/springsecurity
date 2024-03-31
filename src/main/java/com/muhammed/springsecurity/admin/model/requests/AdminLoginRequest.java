package com.muhammed.springsecurity.admin.model.requests;

import com.muhammed.springsecurity.annotations.PasswordPattern;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record AdminLoginRequest(
        @Email
        @NotEmpty
        String email,

        @PasswordPattern
        @NotEmpty
        String password
) {
}
