package com.muhammed.springsecurity.admin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.muhammed.springsecurity.abstracts.AbstractTestcontainers;
import com.muhammed.springsecurity.admin.model.requests.AdminLoginRequest;
import com.muhammed.springsecurity.admin.model.requests.AdminRegistrationRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;
import java.util.stream.Stream;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
class AdminControllerTest extends AbstractTestcontainers {

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    public static final Random RANDOM = new Random();
    public static final String ADMIN_PATH = "/api/v1/admins";

//    @WithMockUser("spring")
//    @Test
//    public void givenAuthRequestOnPrivateService_shouldSucceedWith200() throws Exception {
//        mvc.perform(get("/private/hello").contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//    }

    @Test
    void Given_ValidAdminRegistrationRequest_When_Register_Then_ReturnAdminRegistrationResponse() throws Exception {
        //Given
        AdminRegistrationRequest adminRegistrationRequest = new AdminRegistrationRequest(
                "admin" + RANDOM.nextInt(1000) + "@example.com",
                "Password1.",
                "dummyDepartment"
        );

        //Then
        mockMvc.perform(post(ADMIN_PATH + "/register")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adminRegistrationRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.access_token").exists())
                .andExpect(jsonPath("$.refresh_token").exists());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidRegistrationData")
    void Given_InValidAdminRegistrationRequest_When_Register_Then_ReturnBadRequest(
            String email,
            String password,
            String department,
            String expectedErrorMessage)
            throws Exception {

        //Given
        AdminRegistrationRequest request = new AdminRegistrationRequest(email, password, department);

        //Then
        mockMvc.perform(post(ADMIN_PATH + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors[*]")
                        .value(expectedErrorMessage));
    }

    @Test
    void Given_ValidAdminLoginRequest_When_Login_Then_ReturnAdminLoginResponse() throws Exception {
        //Given
        String email = "admin" + RANDOM.nextInt(1000) + "@example.com";
        String password = "Password1.";

        AdminRegistrationRequest adminRegistrationRequest = new AdminRegistrationRequest(
                email,
                password,
                "dummyDepartment"
        );

        AdminLoginRequest adminLoginRequest = new AdminLoginRequest(email, password);

        registerAdmin(adminRegistrationRequest);

        //Then
        mockMvc.perform(post(ADMIN_PATH + "/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adminLoginRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").exists())
                .andExpect(jsonPath("$.refresh_token").exists());
    }

    @Test
    void Given_NotRegistered_When_Login_Then_ReturnUnauthorized() throws Exception {
        //Given
        AdminLoginRequest request = new AdminLoginRequest("notregistered@example.com", "Password1.");

        //Then
        mockMvc.perform(post(ADMIN_PATH + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors[*]")
                        .value("Bad credentials"));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidLoginData")
    void Given_InvalidParameters_When_Login_Then_ReturnBadRequest(String email, String password, String expectedErrorMessage) throws Exception {
        //Given
        AdminLoginRequest request = new AdminLoginRequest(email, password);

        //Then
        mockMvc.perform(post(ADMIN_PATH + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors[*]")
                        .value(expectedErrorMessage));
    }

    @ParameterizedTest
    @CsvSource({
            "correct@email.com, wrong@email.com, Password1., Password1.",
            "register@email.com, register@email.com, Password1., wrongPassword1."
    })
    void Given_WrongEmailOrPassword_When_Login_Then_ReturnBadRequest(
            String registerEmail,
            String loginEmail,
            String registerPassword,
            String loginPassword) throws Exception {
        //Given

        AdminRegistrationRequest adminRegistrationRequest = new AdminRegistrationRequest(
                registerEmail,
                registerPassword,
                "dummyDepartment"
        );

        AdminLoginRequest adminLoginRequest = new AdminLoginRequest(loginEmail, loginPassword);

        registerAdmin(adminRegistrationRequest);

        //Then
        mockMvc.perform(post(ADMIN_PATH + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adminLoginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors[*]")
                        .value("Bad credentials"));
    }

    private void registerAdmin(AdminRegistrationRequest adminRegistrationRequest) throws Exception {
        mockMvc.perform(post(ADMIN_PATH + "/register")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adminRegistrationRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated());
    }

    private static Stream<Arguments> provideInvalidRegistrationData() {
        return AdminTestDataProvider.provideInvalidRegistrationData();
    }

    private static Stream<Arguments> provideInvalidLoginData() {
        return AdminTestDataProvider.provideInvalidLoginData();
    }
}