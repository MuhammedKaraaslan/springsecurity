package com.muhammed.springsecurity.admin.model.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AdminRegistrationResponse(
        @JsonProperty("access_token")
        String accessToken,

        @JsonProperty("refresh_token")
        String refreshToken
) {
}