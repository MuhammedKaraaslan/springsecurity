package com.muhammed.springsecurity.admin.service.abstracts;

import com.muhammed.springsecurity.admin.model.requests.AdminRegistrationRequest;
import com.muhammed.springsecurity.admin.model.responses.AdminRegistrationResponse;

public interface AdminService {

    AdminRegistrationResponse register(AdminRegistrationRequest adminRegistrationRequest);

}
