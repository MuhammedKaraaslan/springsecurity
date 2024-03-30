package com.muhammed.springsecurity.user.business.abstracts;

import com.muhammed.springsecurity.user.model.entities.User;
import com.muhammed.springsecurity.user.model.responses.UserLoginResponse;
import com.muhammed.springsecurity.user.model.responses.UserRegistrationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService {
    UserRegistrationResponse register(User user);

    UserLoginResponse login(String email, String password);

    void refreshToken(HttpServletRequest request, HttpServletResponse response);
}
