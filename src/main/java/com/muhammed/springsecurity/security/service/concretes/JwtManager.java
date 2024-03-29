package com.muhammed.springsecurity.security.service.concretes;

import com.muhammed.springsecurity.security.config.JwtProperties;
import com.muhammed.springsecurity.security.dataAccess.abstracts.TokenDao;
import com.muhammed.springsecurity.security.model.entities.Token;
import com.muhammed.springsecurity.security.service.abstracts.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

/**
 * Service class for managing JWT tokens, including token generation, validation, and extraction of claims.
 */
@Service
public class JwtManager implements JwtService {

    private final TokenDao tokenDao;
    private final JwtProperties jwtProperties;

    public JwtManager(@Qualifier("token-jpa") TokenDao tokenDao,
                      JwtProperties jwtProperties) {
        this.tokenDao = tokenDao;
        this.jwtProperties = jwtProperties;
    }

    @Override
    public List<Token> findAllValidTokenByUser(Integer userId) {
        return this.tokenDao.findAllValidTokenByUser(userId);
    }

    @Override
    public Optional<Token> findByToken(String token) {
        return this.tokenDao.findByToken(token);
    }

    @Override
    public Token save(Token token) {
        return this.tokenDao.save(token);
    }

    @Override
    public void saveAll(List<Token> validUserTokens) {
        this.tokenDao.saveAll(validUserTokens);
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Generates a JWT token with additional custom claims for the given UserDetails object.
     */
    @Override
    public String generateToken(Map<String, Objects> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, this.jwtProperties.getExpiration());
    }

    @Override
    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, this.jwtProperties.getRefreshTokenExpiration());
    }

    @Override
    public boolean isJwtTokenValid(String token, UserDetails userDetails) {
        return isTokenContainsUser(token, userDetails)
                && !isTokenExpired(token)
                && isTokenNotExpiredOrRevoked(token);
    }

    @Override
    public boolean isRefreshTokenValid(String token, UserDetails userDetails) {
        return isTokenContainsUser(token, userDetails)
                && !isTokenExpired(token);
    }

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private String buildToken(Map<String, Objects> extraClaims, UserDetails userDetails, long expiration) {
        long currentDateMillis = System.currentTimeMillis();

        return Jwts
                .builder()
                .setClaims(extraClaims)
                .claim("roles", userDetails.getAuthorities().toString())
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(currentDateMillis))
                .setExpiration(new Date(currentDateMillis + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiredDate(token).before(new Date());
    }

    private boolean isTokenContainsUser(String token, UserDetails userDetails) {
        return (extractUsername(token).equals(userDetails.getUsername()));
    }

    private boolean isTokenNotExpiredOrRevoked(String token) {
        return tokenDao.findByToken(token)
                .map(t -> !t.isExpired() && !t.isRevoked())
                .orElse(false);
    }

    private Date extractExpiredDate(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts a specific claim from a JWT token using a claims resolver function.
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractAllClaims(token));
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        return Keys.hmacShaKeyFor(this.jwtProperties.getSecretKey().getBytes());
    }
}
