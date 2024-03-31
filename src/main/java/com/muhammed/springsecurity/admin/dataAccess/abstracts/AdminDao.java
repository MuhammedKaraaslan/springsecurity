package com.muhammed.springsecurity.admin.dataAccess.abstracts;

import com.muhammed.springsecurity.admin.model.entities.Admin;

import java.util.Optional;

public interface AdminDao {

    Admin save(Admin admin);

    Optional<Admin> findByEmail(String email);

    boolean existsAdminByEmail(String email);

}
