package com.muhammed.springsecurity.admin.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muhammed.springsecurity.admin.model.requests.AdminLoginRequest;
import com.muhammed.springsecurity.admin.model.requests.AdminRegistrationRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
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
class AdminControllerTest {

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    private static final Random RANDOM = new Random();
    private static final String ADMIN_PATH = "/api/v1/admins";

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
        AdminLoginRequest request = new AdminLoginRequest(
                "admin" + RANDOM.nextInt(1000) + "@example.com",
                "Password1.");

        //Then
        mockMvc.perform(post(ADMIN_PATH + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors[*]")
                        .value(String.format("Admin not found with email: %s", request.email())));
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

    @Test
    void Given_WrongPassword_When_Login_Then_ReturnBadRequest() throws Exception {
        //Given

        String email = "admin" + RANDOM.nextInt(1000) + "@example.com";
        String registerPassword = "Password1.";
        String loginPassword = "LoginPassword1.";
        AdminRegistrationRequest adminRegistrationRequest = new AdminRegistrationRequest(
                email,
                registerPassword,
                "dummyDepartment"
        );

        AdminLoginRequest adminLoginRequest = new AdminLoginRequest(email, loginPassword);

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

    @Test
    void Given_ValidAdminRefreshTokenRequest_When_RefreshToken_Then_ReturnAdminRefreshTokenResponse() throws Exception {
        //Given
        AdminRegistrationRequest adminRegistrationRequest = new AdminRegistrationRequest(
                "admin" + RANDOM.nextInt(1000) + "@example.com",
                "Password1.",
                "dummyDepartment"
        );

        String accessKey = registerAdmin(adminRegistrationRequest);

        //Then
        mockMvc.perform(post(ADMIN_PATH + "/refresh-token")
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessKey))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").exists());
    }

    private String registerAdmin(AdminRegistrationRequest adminRegistrationRequest) throws Exception {
        MvcResult result = mockMvc.perform(post(ADMIN_PATH + "/register")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adminRegistrationRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        return jsonNode.get("access_token").asText();
    }

    private static Stream<Arguments> provideInvalidRegistrationData() {
        return AdminTestDataProvider.provideInvalidRegistrationData();
    }

    private static Stream<Arguments> provideInvalidLoginData() {
        return AdminTestDataProvider.provideInvalidLoginData();
    }
}