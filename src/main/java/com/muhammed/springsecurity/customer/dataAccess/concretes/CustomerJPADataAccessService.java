package com.muhammed.springsecurity.customer.dataAccess.concretes;

import com.muhammed.springsecurity.customer.dataAccess.abstracts.CustomerDao;
import com.muhammed.springsecurity.customer.dataAccess.abstracts.CustomerRepository;
import com.muhammed.springsecurity.customer.model.entities.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("customer-jpa")
@RequiredArgsConstructor
public class CustomerJPADataAccessService implements CustomerDao {

    private final CustomerRepository customerRepository;

    @Override
    public Customer save(Customer customer) {
        return this.customerRepository.save(customer);
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        return this.customerRepository.findByEmail(email);
    }

    @Override
    public boolean existsCustomerByEmail(String email) {
        return this.customerRepository.existsCustomerByEmail(email);
    }
}
