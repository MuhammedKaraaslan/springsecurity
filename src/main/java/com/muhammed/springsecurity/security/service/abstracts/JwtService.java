package com.muhammed.springsecurity.security.service.abstracts;

import com.muhammed.springsecurity.security.model.entities.Token;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * This interface defines operations related to JSON Web Token (JWT) service.
 */
public interface JwtService {

    List<Token> findAllValidTokenByUser(Integer userId);

    Optional<Token> findByToken(String token);

    Token save(Token token);

    void saveAll(List<Token> validUserTokens);

    String generateToken(UserDetails userDetails);

    String generateToken(Map<String, Objects> extraClaims, UserDetails userDetails);

    String generateRefreshToken(UserDetails userDetails);

    boolean isJwtTokenValid(String token, UserDetails userDetails);

    boolean isRefreshTokenValid(String token, UserDetails userDetails);

    String extractUsername(String token);

}
