package com.muhammed.springsecurity.user.business.abstracts;

import com.muhammed.springsecurity.user.model.entities.User;
import com.muhammed.springsecurity.user.model.responses.UserRegistrationResponse;

public interface UserService {
    UserRegistrationResponse register(User user);
}
