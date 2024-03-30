package com.muhammed.springsecurity.user.model.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserRefreshTokenResponse(
        @JsonProperty("access_token")
        String accessToken
) {
}