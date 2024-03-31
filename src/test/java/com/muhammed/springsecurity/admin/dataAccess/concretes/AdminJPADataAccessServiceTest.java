package com.muhammed.springsecurity.admin.dataAccess.concretes;

import com.muhammed.springsecurity.admin.dataAccess.abstracts.AdminRepository;
import com.muhammed.springsecurity.admin.model.entities.Admin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminJPADataAccessServiceTest {

    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private AdminJPADataAccessService underTest;

    @BeforeEach
    void setUp() {
        reset(adminRepository);
    }

    @Test
    void Given_ValidAdmin_When_SaveIsCalled_Then_ReturnSavedAdmin() {
        //Given
        String email = "test@email.com";
        Admin expected = new Admin(email, "Password1.", "dummyDepartment");

        when(adminRepository.save(expected)).thenReturn(expected);

        // Then
        assertEquals(expected, underTest.save(expected));
        verify(adminRepository, times(1)).save(expected);
    }

    @Test
    void Given_AdminExistsByEmail_When_FindByEmailIsCalled_Then_ReturnAdmin() {
        //Given
        String email = "test@email.com";
        Admin expected = new Admin(email, "Password1.", "dummyDepartment");

        when(adminRepository.findByEmail(email)).thenReturn(Optional.of(expected));

        // When
        Optional<Admin> foundCustomer = underTest.findByEmail(email);

        // Then
        assertTrue(foundCustomer.isPresent());
        assertEquals(expected, foundCustomer.get());
    }

    @Test
    void Given_AdminDoesNotExistByEmail_When_FindByEmailIsCalled_Then_ReturnEmptyOptional() {
        // Given
        String email = "nonexistent@email.com";

        when(adminRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When
        Optional<Admin> foundCustomer = underTest.findByEmail(email);

        // Then
        assertTrue(foundCustomer.isEmpty());
    }

    @Test
    void Given_AdminExistsByEmail_When_ExistAdminByEmailCalled_Then_ReturnTrue() {
        //Given
        String email = "test@email.com";

        when(adminRepository.existsAdminByEmail(email)).thenReturn(true);

        // Then
        assertTrue(underTest.existsAdminByEmail(email));
    }

    @Test
    void Given_AdminDoesNotExistsByEmail_When_ExistAdminByEmailCalled_Then_ReturnFalse() {
        // Given
        String email = "nonexistent@email.com";

        when(adminRepository.existsAdminByEmail(email)).thenReturn(false);

        // Then
        assertFalse(underTest.existsAdminByEmail(email));
    }
}