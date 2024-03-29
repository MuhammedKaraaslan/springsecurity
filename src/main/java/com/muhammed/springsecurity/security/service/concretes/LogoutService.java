package com.muhammed.springsecurity.security.service.concretes;

import com.muhammed.springsecurity.exceptions.ResourceNotFoundException;
import com.muhammed.springsecurity.security.model.entities.Token;
import com.muhammed.springsecurity.security.service.abstracts.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
public class LogoutService implements LogoutHandler {

    private final JwtService jwtService;

    public LogoutService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        String jwtToken = authHeader.substring(7);

        Token storedToken = jwtService.findByToken(jwtToken)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Token not found!"));

        if (storedToken != null && isTokenValid(jwtToken)) {
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            jwtService.save(storedToken);
            SecurityContextHolder.clearContext();
        }
    }

    private boolean isTokenValid(String jwtToken) {
        return jwtService.findByToken(jwtToken)
                .map(t -> !t.isExpired() && !t.isRevoked())
                .orElse(false);
    }
}