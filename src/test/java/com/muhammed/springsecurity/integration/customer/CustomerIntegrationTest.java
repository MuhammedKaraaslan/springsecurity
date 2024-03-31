package com.muhammed.springsecurity.integration.customer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muhammed.springsecurity.customer.model.requests.CustomerLoginRequest;
import com.muhammed.springsecurity.customer.model.requests.CustomerRegistrationRequest;
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
class CustomerIntegrationTest {

    private static final Random RANDOM = new Random();
    private static final String CUSTOMERS_PATH = "/api/v1/customers";

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void Given_ValidCustomerRegistrationRequest_When_Register_Then_ReturnCustomerRegistrationResponse() throws Exception {
        //Given
        CustomerRegistrationRequest customerRegistrationRequest = new CustomerRegistrationRequest(
                "customer" + RANDOM.nextInt(1000) + "@example.com",
                "Password1.",
                "dummyFirstName",
                "dummyLastName"
        );

        //Then
        mockMvc.perform(post(CUSTOMERS_PATH + "/register")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerRegistrationRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.access_token").exists())
                .andExpect(jsonPath("$.refresh_token").exists());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidRegistrationData")
    void Given_InValidCustomerRegistrationRequest_When_Register_Then_ReturnBadRequest(
            String email,
            String password,
            String firstName,
            String lastName,
            String expectedErrorMessage)
            throws Exception {

        //Given
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(email, password, firstName, lastName);

        //Then
        mockMvc.perform(post(CUSTOMERS_PATH + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors[*]")
                        .value(expectedErrorMessage));
    }

    @Test
    void Given_ValidCustomerLoginRequest_When_Login_Then_ReturnCustomerLoginResponse() throws Exception {
        //Given
        String email = "customer" + RANDOM.nextInt(1000) + "@example.com";
        String password = "Password1.";

        CustomerRegistrationRequest customerRegistrationRequest = new CustomerRegistrationRequest(
                email,
                password,
                "dummyFirstName",
                "dummyLastName"
        );

        CustomerLoginRequest customerLoginRequest = new CustomerLoginRequest(email, password);

        registerCustomer(customerRegistrationRequest);

        //Then
        mockMvc.perform(post(CUSTOMERS_PATH + "/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerLoginRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").exists())
                .andExpect(jsonPath("$.refresh_token").exists());
    }

    @Test
    void Given_NotRegistered_When_Login_Then_ReturnUnauthorized() throws Exception {
        //Given
        CustomerLoginRequest request = new CustomerLoginRequest(
                "customer" + RANDOM.nextInt(1000) + "@example.com",
                "Password1.");

        //Then
        mockMvc.perform(post(CUSTOMERS_PATH + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors[*]")
                        .value(String.format("Customer not found with email: %s", request.email())));
    }

    @Test
    void Given_WrongPassword_When_Login_Then_ReturnBadRequest() throws Exception {
        //Given

        String email = "customer" + RANDOM.nextInt(1000) + "@example.com";
        String registerPassword = "Password1.";
        String loginPassword = "LoginPassword1.";
        CustomerRegistrationRequest registrationRequest = new CustomerRegistrationRequest(
                email,
                registerPassword,
                "dummyFirstName",
                "dummyLastName"
        );

        CustomerLoginRequest loginRequest = new CustomerLoginRequest(email, loginPassword);

        registerCustomer(registrationRequest);

        //Then
        mockMvc.perform(post(CUSTOMERS_PATH + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors[*]")
                        .value("Bad credentials"));
    }

    @Test
    void Given_ValidAdminRefreshTokenRequest_When_RefreshToken_Then_ReturnAdminRefreshTokenResponse() throws Exception {
        //Given
        CustomerRegistrationRequest registrationRequest = new CustomerRegistrationRequest(
                "customer" + RANDOM.nextInt(1000) + "@example.com",
                "Password1.",
                "dummyFirstName",
                "dummyLastName"
        );

        String accessKey = registerCustomer(registrationRequest);

        //Then
        mockMvc.perform(post(CUSTOMERS_PATH + "/refresh-token")
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessKey))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").exists());
    }

    private static Stream<Arguments> provideInvalidRegistrationData() {
        return CustomerTestDataProvider.provideInvalidRegistrationData();
    }

    private String registerCustomer(CustomerRegistrationRequest registrationRequest) throws Exception {
        MvcResult result = mockMvc.perform(post(CUSTOMERS_PATH + "/register")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        return jsonNode.get("access_token").asText();
    }


}