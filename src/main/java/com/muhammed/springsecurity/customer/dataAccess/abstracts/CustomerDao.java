package com.muhammed.springsecurity.customer.dataAccess.abstracts;

import com.muhammed.springsecurity.customer.model.entities.Customer;

import java.util.Optional;

public interface CustomerDao {

    Customer save(Customer customer);

    Optional<Customer> findByEmail(String email);

    boolean existsCustomerByEmail(String email);
}
