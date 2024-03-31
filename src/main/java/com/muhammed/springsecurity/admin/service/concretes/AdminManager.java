package com.muhammed.springsecurity.admin.service.concretes;

import com.muhammed.springsecurity.admin.dataAccess.abstracts.AdminDao;
import com.muhammed.springsecurity.admin.model.requests.AdminLoginRequest;
import com.muhammed.springsecurity.admin.model.requests.AdminRegistrationRequest;
import com.muhammed.springsecurity.admin.model.responses.AdminLoginResponse;
import com.muhammed.springsecurity.admin.model.responses.AdminRegistrationResponse;
import com.muhammed.springsecurity.admin.service.abstracts.AdminService;
import com.muhammed.springsecurity.exceptions.ResourceNotFoundException;
import com.muhammed.springsecurity.user.business.abstracts.UserService;
import com.muhammed.springsecurity.user.model.responses.UserLoginResponse;
import com.muhammed.springsecurity.user.model.responses.UserRegistrationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminManager implements AdminService {

    private final AdminDao adminDao;
    private final UserService userService;

public AdminManager(@Qualifier("admin-jpa") AdminDao adminDao,
                    UserService userService) {
        this.adminDao = adminDao;
        this.userService = userService;
    }

    @Override
    @Transactional
    public AdminRegistrationResponse register(AdminRegistrationRequest adminRegistrationRequest) {
        // Registration logic specific to admins
        // Validate admin-specific fields, etc.
        // Optionally, perform additional tasks specific to admins

        UserRegistrationResponse userRegistrationResponse = userService.register(
                AdminRegistrationRequest.toEntity(adminRegistrationRequest)
        );

        return new AdminRegistrationResponse(
                userRegistrationResponse.accessToken(),
                userRegistrationResponse.refreshToken()
        );
    }

    @Override
    public AdminLoginResponse login(AdminLoginRequest adminLoginRequest) {
        // Login logic specific to admins
        // Validate admin-specific fields, etc.
        // Optionally, perform additional tasks specific to admins

        if (! adminDao.existsAdminByEmail(adminLoginRequest.email())) {
            throw new ResourceNotFoundException(String.format("Admin not found with email: %s",
                    adminLoginRequest.email()));
        }

        UserLoginResponse userLoginResponse = userService.login(
                adminLoginRequest.email(),
                adminLoginRequest.password()
        );

        return new AdminLoginResponse(
                userLoginResponse.accessToken(),
                userLoginResponse.refreshToken()
        );
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        this.userService.refreshToken(request, response);
    }
}