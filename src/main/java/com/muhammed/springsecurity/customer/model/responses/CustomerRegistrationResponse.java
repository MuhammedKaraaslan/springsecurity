package com.muhammed.springsecurity.customer.model.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CustomerRegistrationResponse(
        @JsonProperty("access_token")
        String accessToken,

        @JsonProperty("refresh_token")
        String refreshToken
) {
}
