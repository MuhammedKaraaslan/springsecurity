package com.muhammed.springsecurity.admin.service.concretes;

import com.muhammed.springsecurity.admin.dataAccess.abstracts.AdminDao;
import com.muhammed.springsecurity.admin.model.entities.Admin;
import com.muhammed.springsecurity.admin.model.requests.AdminRegistrationRequest;
import com.muhammed.springsecurity.admin.model.responses.AdminRegistrationResponse;
import com.muhammed.springsecurity.admin.service.abstracts.AdminService;
import com.muhammed.springsecurity.security.model.entities.Token;
import com.muhammed.springsecurity.security.model.enums.TokenType;
import com.muhammed.springsecurity.security.service.abstracts.JwtService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminManager implements AdminService {

    private final AdminDao adminDao;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AdminManager(@Qualifier("admin-jpa") AdminDao adminDao,
                        JwtService jwtService,
                        PasswordEncoder passwordEncoder) {
        this.adminDao = adminDao;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public AdminRegistrationResponse register(AdminRegistrationRequest adminRegistrationRequest) {
        Admin admin = AdminRegistrationRequest.toEntity(adminRegistrationRequest);
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));

        String accessToken = jwtService.generateToken(admin);
        String refreshToken = jwtService.generateRefreshToken(admin);

        Admin savedAdmin = adminDao.save(admin);

        saveToken(savedAdmin, accessToken);

        return new AdminRegistrationResponse(accessToken, refreshToken);
    }

    private void saveToken(Admin admin, String accessToken) {
        Token token = Token.builder()
                .user(admin)
                .token(accessToken)
                .tokenType(TokenType.BEARER)
                .build();

        this.jwtService.save(token);
    }
}