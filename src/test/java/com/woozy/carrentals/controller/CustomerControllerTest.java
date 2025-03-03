package com.woozy.carrentals.controller;

import com.woozy.carrentals.annotations.WithMockCustomer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.woozy.carrentals.constants.EndpointBuilder;
import com.woozy.carrentals.constants.EndpointPathVariable;
import com.woozy.carrentals.security.JwtService;
import com.woozy.carrentals.security.WebSecurity;
import com.woozy.carrentals.service.CustomerService;
import com.woozy.carrentals.ui.controller.CustomerController;
import com.woozy.carrentals.utils.TestDataGenerator;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static com.woozy.carrentals.exceptions.errormessages.ControllerDetailMsg.USER_NOT_FOUND;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
@Import(WebSecurity.class)
public class CustomerControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private CustomerService customerService;
    @MockitoBean
    private JwtService jwtService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private EndpointBuilder endpointBuilder;
    private String userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID().toString();
    }

    @SneakyThrows
    @Test
    @WithMockCustomer
    void getCustomer_validCustomerId_returnsCustomer() {
        var customerResponseDto = TestDataGenerator.generateCustomerResponse();
        customerResponseDto.setUserId(userId);
        when(customerService.getCustomer(userId)).thenReturn(customerResponseDto);

        mockMvc.perform(get(endpointBuilder.buildCustomerEndpointFor(EndpointPathVariable.USER_ID), userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(customerResponseDto.getUserId()))
                .andExpect(jsonPath("$.email").value(customerResponseDto.getEmail()))
                .andExpect(jsonPath("$.firstName").value(customerResponseDto.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(customerResponseDto.getLastName()))
                .andExpect(jsonPath("$.mobileNumber").value(customerResponseDto.getMobileNumber()))
                .andExpect(jsonPath("$.address").value(customerResponseDto.getAddress()));

        verify(customerService, times(1)).getCustomer(userId);
    }

    @SneakyThrows
    @Test
    @WithMockCustomer
    void getCustomer_invalidCustomerId_returnsBadRequest() {
        when(customerService.getCustomer(userId)).thenThrow(new UsernameNotFoundException(USER_NOT_FOUND));

        mockMvc.perform(get(endpointBuilder.buildCustomerEndpointFor(EndpointPathVariable.USER_ID), userId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value(USER_NOT_FOUND));

        verify(customerService, times(1)).getCustomer(userId);
    }

    @SneakyThrows
    @Test
    void getCustomer_unauthorized_returnsUnauthorized() {
        mockMvc.perform(get(endpointBuilder.buildCustomerEndpointFor(EndpointPathVariable.USER_ID), userId))
                .andExpect(status().isUnauthorized());

        verify(customerService, times(0)).getCustomer(userId);
    }

    @SneakyThrows
    @WithMockCustomer
    @Test
    void updateCustomer_validUpdateDetails_returnsUpdatedCustomer() {
        var updateCustomerRequestDto = TestDataGenerator.generateUpdateCustomerRequestDto();
        var customerResponseDto = TestDataGenerator.generateCustomerResponse();
        customerResponseDto.setUserId(userId);
        customerResponseDto.setEmail(updateCustomerRequestDto.getEmail());
        customerResponseDto.setMobileNumber(updateCustomerRequestDto.getMobileNumber());
        customerResponseDto.setAddress(updateCustomerRequestDto.getAddress());

        when(customerService.updateCustomer(userId, updateCustomerRequestDto)).thenReturn(customerResponseDto);

        mockMvc.perform(put(endpointBuilder.buildCustomerEndpointFor(EndpointPathVariable.USER_ID), userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCustomerRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(customerResponseDto.getUserId()))
                .andExpect(jsonPath("$.email").value(customerResponseDto.getEmail()))
                .andExpect(jsonPath("$.firstName").value(customerResponseDto.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(customerResponseDto.getLastName()))
                .andExpect(jsonPath("$.mobileNumber").value(customerResponseDto.getMobileNumber()))
                .andExpect(jsonPath("$.address").value(customerResponseDto.getAddress()));

        verify(customerService, times(1)).updateCustomer(userId, updateCustomerRequestDto);
    }

    @SneakyThrows
    @WithMockCustomer
    @Test
    void updateCustomer_invalidUserId_returnsBadRequest() {
        var updateCustomerRequestDto = TestDataGenerator.generateUpdateCustomerRequestDto();
        when(customerService.updateCustomer(userId, updateCustomerRequestDto)).thenThrow(new UsernameNotFoundException(USER_NOT_FOUND));

        mockMvc.perform(put(endpointBuilder.buildCustomerEndpointFor(EndpointPathVariable.USER_ID), userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCustomerRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value(USER_NOT_FOUND));

        verify(customerService, times(1)).updateCustomer(userId, updateCustomerRequestDto);
    }

    @SneakyThrows
    @WithMockCustomer
    @Test
    void deleteCustomer_validUserId_returnsNoContent() {
        doNothing().when(customerService).deleteCustomer(userId);

        mockMvc.perform(delete(endpointBuilder.buildCustomerEndpointFor(EndpointPathVariable.USER_ID), userId))
                .andExpect(status().isNoContent());
        verify(customerService, times(1)).deleteCustomer(userId);
    }

    @SneakyThrows
    @WithMockCustomer
    @Test
    void deleteCustomer_invalidUserId_returnsBadRequest() {
        doThrow(new UsernameNotFoundException(USER_NOT_FOUND)).when(customerService).deleteCustomer(userId);

        mockMvc.perform(delete(endpointBuilder.buildCustomerEndpointFor(EndpointPathVariable.USER_ID), userId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value(USER_NOT_FOUND));

        verify(customerService, times(1)).deleteCustomer(userId);
    }
}
