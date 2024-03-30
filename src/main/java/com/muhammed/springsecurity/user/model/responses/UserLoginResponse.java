package com.muhammed.springsecurity.user.model.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserLoginResponse(
        @JsonProperty("access_token")
        String accessToken,

        @JsonProperty("refresh_token")
        String refreshToken
) {
}
