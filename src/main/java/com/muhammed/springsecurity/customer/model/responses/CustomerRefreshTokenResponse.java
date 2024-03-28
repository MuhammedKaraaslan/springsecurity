package com.muhammed.springsecurity.customer.model.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CustomerRefreshTokenResponse(
        @JsonProperty("access_token")
        String accessToken
) {
}
