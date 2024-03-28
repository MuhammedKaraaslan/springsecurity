package com.muhammed.springsecurity.customer.dataAccess.abstracts;

import com.muhammed.springsecurity.customer.model.entities.Customer;

public interface CustomerDao {

    Customer save(Customer customer);

}
