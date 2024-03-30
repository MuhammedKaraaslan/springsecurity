package com.muhammed.springsecurity.admin.service.concretes;

import com.muhammed.springsecurity.admin.model.requests.AdminLoginRequest;
import com.muhammed.springsecurity.admin.model.requests.AdminRegistrationRequest;
import com.muhammed.springsecurity.admin.model.responses.AdminLoginResponse;
import com.muhammed.springsecurity.admin.model.responses.AdminRegistrationResponse;
import com.muhammed.springsecurity.admin.service.abstracts.AdminService;
import com.muhammed.springsecurity.user.business.abstracts.UserService;
import com.muhammed.springsecurity.user.model.responses.UserLoginResponse;
import com.muhammed.springsecurity.user.model.responses.UserRegistrationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminManager implements AdminService {

    private final UserService userService;

    public AdminManager(UserService userService) {
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

        UserLoginResponse userLoginResponse = userService.login(
                adminLoginRequest.email(),
                adminLoginRequest.password()
        );

        return new AdminLoginResponse(
                userLoginResponse.accessToken(),
                userLoginResponse.refreshToken()
        );
    }
}