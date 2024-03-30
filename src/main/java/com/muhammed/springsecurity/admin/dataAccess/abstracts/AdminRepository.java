package com.muhammed.springsecurity.admin.dataAccess.abstracts;

import com.muhammed.springsecurity.admin.model.entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Integer> {

    Optional<Admin> findByEmail(String email);

}
