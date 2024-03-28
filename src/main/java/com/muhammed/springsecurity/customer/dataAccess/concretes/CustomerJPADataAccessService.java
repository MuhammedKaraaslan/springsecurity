package com.muhammed.springsecurity.customer.dataAccess.concretes;

import com.muhammed.springsecurity.customer.dataAccess.abstracts.CustomerDao;
import com.muhammed.springsecurity.customer.dataAccess.abstracts.CustomerRepository;
import com.muhammed.springsecurity.customer.model.entities.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository("customer-jpa")
@RequiredArgsConstructor
public class CustomerJPADataAccessService implements CustomerDao {

    private final CustomerRepository customerRepository;

    @Override
    public Customer save(Customer customer) {
        return this.customerRepository.save(customer);
    }
}
