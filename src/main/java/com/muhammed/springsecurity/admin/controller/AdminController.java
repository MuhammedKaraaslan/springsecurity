package com.muhammed.springsecurity.admin.controller;

import com.muhammed.springsecurity.admin.model.requests.AdminRegistrationRequest;
import com.muhammed.springsecurity.admin.model.responses.AdminRegistrationResponse;
import com.muhammed.springsecurity.admin.service.abstracts.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

}
