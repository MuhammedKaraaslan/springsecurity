package com.muhammed.springsecurity.customer.model.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CustomerLoginResponse(
        @JsonProperty("access_token")
        String accessToken,

        @JsonProperty("refresh_token")
        String refreshToken
) {
}
