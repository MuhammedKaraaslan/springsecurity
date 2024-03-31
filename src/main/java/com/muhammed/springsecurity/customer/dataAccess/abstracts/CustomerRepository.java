package com.muhammed.springsecurity.customer.dataAccess.abstracts;

import com.muhammed.springsecurity.customer.model.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    Optional<Customer> findByEmail(String email);

    boolean existsCustomerByEmail(String email);
}
