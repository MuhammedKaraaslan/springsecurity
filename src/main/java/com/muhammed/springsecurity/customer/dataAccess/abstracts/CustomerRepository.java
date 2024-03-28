package com.muhammed.springsecurity.customer.dataAccess.abstracts;

import com.muhammed.springsecurity.customer.model.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}
