package com.muhammed.springsecurity.admin.model.requests;

import com.muhammed.springsecurity.admin.model.entities.Admin;
import com.muhammed.springsecurity.annotations.PasswordPattern;
import com.muhammed.springsecurity.annotations.UniqueEmail;
import com.muhammed.springsecurity.annotations.ValidName;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record AdminRegistrationRequest(
        @Email
        @UniqueEmail
        @NotEmpty
        String email,

        @PasswordPattern
        @NotEmpty
        String password,

        @ValidName
        String department
) {
    public static Admin toEntity(AdminRegistrationRequest request) {
        return new Admin(
                request.email(),
                request.password(),
                request.department()
        );
    }
}
