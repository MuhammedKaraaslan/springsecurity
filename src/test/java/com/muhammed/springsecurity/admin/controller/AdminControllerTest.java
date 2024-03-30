package com.muhammed.springsecurity.admin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.muhammed.springsecurity.abstracts.AbstractTestcontainers;
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

import java.util.Random;
import java.util.stream.Stream;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
class AdminControllerTest extends AbstractTestcontainers {

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    public static final Random RANDOM = new Random();
    public static final String CUSTOMER_PATH = "/api/v1/admins";

//    @WithMockUser("spring")
//    @Test
//    public void givenAuthRequestOnPrivateService_shouldSucceedWith200() throws Exception {
//        mvc.perform(get("/private/hello").contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//    }

    @Test
    void Given_ValidAdminRegistrationRequest_When_Register_Then_ReturnAdminRegistrationResponse() throws Exception {
        AdminRegistrationRequest adminRegistrationRequest = new AdminRegistrationRequest(
                "admin" + RANDOM.nextInt(1000) + "@example.com",
                "Password1.",
                "dummyDepartment"
        );

        mockMvc.perform(post(CUSTOMER_PATH + "/register")
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
            String email, String password, String department, String expectedErrorMessage) throws Exception {

        AdminRegistrationRequest request = new AdminRegistrationRequest(email, password, department);

        mockMvc.perform(post(CUSTOMER_PATH + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors[*]")
                        .value(expectedErrorMessage));
    }

    private static Stream<Arguments> provideInvalidRegistrationData() {
        return AdminTestDataProvider.provideInvalidRegistrationData();
    }
}