package com.muhammed.springsecurity.admin.dataAccess.abstracts;

import com.muhammed.springsecurity.abstracts.AbstractRepositoryTest;
import com.muhammed.springsecurity.admin.model.entities.Admin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AdminRepositoryTest  extends AbstractRepositoryTest {

    @Autowired
    private AdminRepository underTest;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
    }

    @Test
    void Given_AdminExistsByEmail_When_FindByEmailIsCalled_Then_ReturnAdmin() {
        //Given
        String email = "test@email.com";
        Admin expected = saveAdmin(email, "Password1.", "dummyDepartment");

        // When
        Optional<Admin> foundCustomer = underTest.findByEmail(email);

        // Then
        assertTrue(foundCustomer.isPresent());
        assertEquals(expected, foundCustomer.get());
    }

    @Test
    void Given_AdminDoesNotExistByEmail_When_FindByEmailIsCalled_Then_ReturnEmptyOptional() {
        // When
        Optional<Admin> foundCustomer = underTest.findByEmail("nonexistent@email.com");

        // Then
        assertTrue(foundCustomer.isEmpty());
    }

    @Test
    void Given_AdminExistsByEmail_When_ExistsAdminByEmail_Then_ReturnTrue() {
        //Given
        String email = "test@email.com";
        Admin expected = saveAdmin(email, "Password1.", "dummyDepartment");

        // Then
        assertTrue(underTest.existsAdminByEmail(email));
    }

    @Test
    void Given_AdminDoesNotExistByEmail_When_ExistsAdminByEmail_Then_ReturnFalse() {
        //Given
        String email = "test@email.com";

        // Then
        assertFalse(underTest.existsAdminByEmail(email));
    }

    private Admin saveAdmin(String email, String password, String department) {
        Admin admin = new Admin(email, password, department);
        entityManager.persist(admin);
        entityManager.flush();
        return admin;
    }

}