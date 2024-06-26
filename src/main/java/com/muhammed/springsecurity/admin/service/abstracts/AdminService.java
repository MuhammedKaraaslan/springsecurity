package com.muhammed.springsecurity.admin.service.abstracts;

import com.muhammed.springsecurity.admin.model.requests.AdminLoginRequest;
import com.muhammed.springsecurity.admin.model.requests.AdminRegistrationRequest;
import com.muhammed.springsecurity.admin.model.responses.AdminLoginResponse;
import com.muhammed.springsecurity.admin.model.responses.AdminRegistrationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AdminService {

    AdminRegistrationResponse register(AdminRegistrationRequest adminRegistrationRequest);

    AdminLoginResponse login(AdminLoginRequest adminLoginRequest);

    void refreshToken(HttpServletRequest request, HttpServletResponse response);
}
