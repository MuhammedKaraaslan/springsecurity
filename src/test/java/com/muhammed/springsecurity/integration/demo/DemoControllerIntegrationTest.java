package com.muhammed.springsecurity.integration.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
class DemoControllerIntegrationTest {

    private static final String DEMO_PATH = "/api/v1/demo";
    private static final String CUSTOMER_SECURE_ENDPOINT = "/customer-secure-endpoint";
    private static final String ADMIN_SECURE_ENDPOINT = "/admin-secure-endpoint";
    private static final String ADMIN_OR_CUSTOMER_SECURE_ENDPOINT = "/admin-or-customer-secure-endpoint";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void Given_UnAuthorizedUser_When_PublicEndPoint_Then_ReturnResponse() throws Exception {
        mockMvc.perform(get(DEMO_PATH + "/public-endpoint"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string("Hello from public endpoint"));
    }

    @Test
    @WithMockUser(username = "customer", roles = {"CUSTOMER"})
    public void Given_Customer_When_CustomerSecureEndPoint_Then_ReturnResponse() throws Exception {
        mockMvc.perform(get(DEMO_PATH + CUSTOMER_SECURE_ENDPOINT))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string("Hello from customer secure endpoint"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void Given_Admin_When_CustomerSecureEndPoint_Then_ReturnAccessDenied() throws Exception {
        mockMvc.perform(get(DEMO_PATH + CUSTOMER_SECURE_ENDPOINT))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errors").exists());
    }

    @Test
    public void Given_UnAuthorizedUser_When_CustomerSecureEndPoint_Then_ReturnAccessDenied() throws Exception {
        mockMvc.perform(get(DEMO_PATH + CUSTOMER_SECURE_ENDPOINT))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errors").exists());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void Given_Admin_When_AdminSecureEndPoint_Then_ReturnResponse() throws Exception {
        mockMvc.perform(get(DEMO_PATH + ADMIN_SECURE_ENDPOINT))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string("Hello from admin secure endpoint"));
    }

    @Test
    @WithMockUser(username = "customer", roles = {"CUSTOMER"})
    public void Given_Customer_When_AdminSecureEndPoint_Then_ReturnAccessDenied() throws Exception {
        mockMvc.perform(get(DEMO_PATH + ADMIN_SECURE_ENDPOINT))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errors").exists());
    }

    @Test
    public void Given_UnAuthorizedUser_When_AdminSecureEndPoint_Then_ReturnAccessDenied() throws Exception {
        mockMvc.perform(get(DEMO_PATH + ADMIN_SECURE_ENDPOINT))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errors").exists());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void Given_Admin_When_AdminOrCustomerSecureEndPoint_Then_ReturnResponse() throws Exception {
        mockMvc.perform(get(DEMO_PATH + ADMIN_OR_CUSTOMER_SECURE_ENDPOINT))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string("Hello from admin or customer secure endpoint"));
    }

    @Test
    @WithMockUser(username = "customer", roles = {"CUSTOMER"})
    public void Given_Customer_When_AdminOrCustomerSecureEndPoint_Then_ReturnResponse() throws Exception {
        mockMvc.perform(get(DEMO_PATH + ADMIN_OR_CUSTOMER_SECURE_ENDPOINT))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string("Hello from admin or customer secure endpoint"));
    }

    @Test
    public void Given_UnAuthorizedUser_When_AdminOrCustomerSecureEndPoint_Then_ReturnAccessDenied() throws Exception {
        mockMvc.perform(get(DEMO_PATH + ADMIN_OR_CUSTOMER_SECURE_ENDPOINT))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errors").exists());
    }

}