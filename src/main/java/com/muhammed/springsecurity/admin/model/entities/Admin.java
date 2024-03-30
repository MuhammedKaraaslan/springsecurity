package com.muhammed.springsecurity.admin.model.entities;

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
@Table(name = "admins")
@PrimaryKeyJoinColumn(name = "adminId")
public class Admin extends User {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "department")
    private String department;

    public Admin(String email, String password, String department) {
        super(email, password, Set.of(Role.ADMIN));
        this.department = department;
    }

}
