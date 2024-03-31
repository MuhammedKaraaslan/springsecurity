package com.muhammed.springsecurity.admin.dataAccess.concretes;

import com.muhammed.springsecurity.admin.dataAccess.abstracts.AdminDao;
import com.muhammed.springsecurity.admin.dataAccess.abstracts.AdminRepository;
import com.muhammed.springsecurity.admin.model.entities.Admin;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("admin-jpa")
@RequiredArgsConstructor
public class AdminJPADataAccessService implements AdminDao {

    private final AdminRepository adminRepository;


    @Override
    public Admin save(Admin admin) {
        return this.adminRepository.save(admin);
    }

    @Override
    public Optional<Admin> findByEmail(String email) {
        return this.adminRepository.findByEmail(email);
    }

    @Override
    public boolean existsAdminByEmail(String email) {
        return this.adminRepository.existsAdminByEmail(email);
    }
}
