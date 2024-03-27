package com.muhammed.springsecurity.security.service.abstracts;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.Objects;

/**
 * This interface defines operations related to JSON Web Token (JWT) service.
 */
public interface JwtService {

    String generateToken(UserDetails userDetails);

    String generateToken(Map<String, Objects> extraClaims, UserDetails userDetails);

    String generateRefreshToken(UserDetails userDetails);

    boolean isJwtTokenValid(String token, UserDetails userDetails);

    boolean isRefreshTokenValid(String token, UserDetails userDetails);

    String extractUsername(String token);

}
