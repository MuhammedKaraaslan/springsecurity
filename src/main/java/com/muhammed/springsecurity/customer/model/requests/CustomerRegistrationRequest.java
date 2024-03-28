package com.muhammed.springsecurity.customer.model.requests;

import com.muhammed.springsecurity.annotations.PasswordPattern;
import com.muhammed.springsecurity.annotations.UniqueEmail;
import com.muhammed.springsecurity.annotations.ValidName;
import com.muhammed.springsecurity.customer.model.entities.Customer;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record CustomerRegistrationRequest(
        @Email
        @UniqueEmail
        @NotEmpty
        String email,

        @PasswordPattern
        @NotEmpty
        String password,

        @ValidName
        String firstname,

        @ValidName
        String lastname

) {

    public static Customer toEntity(CustomerRegistrationRequest request) {
        return new Customer(
                request.email(),
                request.password(),
                request.firstname(),
                request.lastname()
        );
    }

}
