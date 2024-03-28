package com.muhammed.springsecurity.customer.model.entities;

import com.muhammed.springsecurity.model.Role;
import com.muhammed.springsecurity.user.model.entities.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)

@Entity
@Table(name = "customers")
@PrimaryKeyJoinColumn(name = "customerId")
public class Customer extends User {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    public Customer(String email, String password, String firstname, String lastname) {
        super(email, password, Set.of(Role.CUSTOMER));
        this.firstname = firstname;
        this.lastname = lastname;
    }
}
