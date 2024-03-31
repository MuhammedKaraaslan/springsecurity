package com.muhammed.springsecurity.admin.controller;

import com.muhammed.springsecurity.admin.model.requests.AdminLoginRequest;
import com.muhammed.springsecurity.admin.model.requests.AdminRegistrationRequest;
import com.muhammed.springsecurity.admin.model.responses.AdminLoginResponse;
import com.muhammed.springsecurity.admin.model.responses.AdminRegistrationResponse;
import com.muhammed.springsecurity.admin.service.abstracts.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admins")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AdminRegistrationResponse> register(
            @Valid @RequestBody AdminRegistrationRequest adminRegistrationRequest) {

        AdminRegistrationResponse response = this.adminService.register(adminRegistrationRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .header(
                        HttpHeaders.AUTHORIZATION,
                        response.accessToken(),
                        response.refreshToken()
                )
                .body(response);
    }

    @PostMapping("/login")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<AdminLoginResponse> login(
            @Valid @RequestBody AdminLoginRequest adminLoginRequest) {
        AdminLoginResponse response = adminService.login(adminLoginRequest);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.AUTHORIZATION,
                        response.accessToken()
                )
                .body(response);
    }

    @PostMapping("/refresh-token")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        this.adminService.refreshToken(request, response);
    }

}
