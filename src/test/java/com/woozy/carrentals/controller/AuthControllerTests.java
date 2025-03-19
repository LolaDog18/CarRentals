package com.woozy.carrentals.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woozy.AuthenticationRequestDto;
import com.woozy.RegisterCustomerRequestDto;
import com.woozy.carrentals.exceptions.CustomerEntityException;
import com.woozy.carrentals.exceptions.errormessages.ControllerDetailMsg;
import com.woozy.carrentals.security.JwtService;
import com.woozy.carrentals.service.AuthenticationService;
import com.woozy.carrentals.shared.dto.response.authentication.AuthenticationResponseDto;
import com.woozy.carrentals.shared.dto.response.customer.CustomerResponseDto;
import com.woozy.carrentals.ui.controller.AuthController;
import com.woozy.carrentals.utils.TestDataGenerator;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static com.woozy.carrentals.constants.AuthEndpointAction.AUTHENTICATE;
import static com.woozy.carrentals.constants.AuthEndpointAction.REGISTER_CUSTOMER;
import static com.woozy.carrentals.constants.EndpointResourcePath.AUTH_PATH;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
public class AuthControllerTests extends ControllerTestBase {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private AuthenticationService authenticationService;
    @MockitoBean
    private JwtService jwtService;
    @Autowired
    private ObjectMapper objectMapper;

    private RegisterCustomerRequestDto requestDto;
    private CustomerResponseDto responseDto;

    @BeforeEach
    void setUp() {
        requestDto = TestDataGenerator.generateRegisterCustomerRequest();
        responseDto = CustomerResponseDto.builder()
                .userId(UUID.randomUUID().toString())
                .email(requestDto.getEmail())
                .firstName(requestDto.getFirstName())
                .lastName(requestDto.getLastName())
                .mobileNumber(requestDto.getMobileNumber())
                .address(requestDto.getAddress())
                .build();
    }

    @SneakyThrows
    @Test
    void registerCustomer_validCustomerDetails_success() {
        when(authenticationService.registerCustomer(any(RegisterCustomerRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post(AUTH_PATH + REGISTER_CUSTOMER)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(responseDto.getUserId()))
                .andExpect(jsonPath("$.email").value(responseDto.getEmail()))
                .andExpect(jsonPath("$.firstName").value(responseDto.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(responseDto.getLastName()))
                .andExpect(jsonPath("$.mobileNumber").value(responseDto.getMobileNumber()))
                .andExpect(jsonPath("$.address").value(responseDto.getAddress()));

        verify(authenticationService, times(1)).registerCustomer(any(RegisterCustomerRequestDto.class));
    }

    @SneakyThrows
    @Test
    void registerCustomer_duplicateEmail_throwsCustomerEntityException() {
        when(authenticationService.registerCustomer(any(RegisterCustomerRequestDto.class)))
                .thenThrow(new CustomerEntityException("The customer email or phone number already exists"));

        mockMvc.perform(post(AUTH_PATH + REGISTER_CUSTOMER)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value(ControllerDetailMsg.EMAIL_MOBILE_EXISTS));

        verify(authenticationService, times(1)).registerCustomer(any(RegisterCustomerRequestDto.class));
    }

    @SneakyThrows
    @Test
    void authenticateUser_duplicateMobileNumber_throwsCustomerEntityException() {
        when(authenticationService.authenticateCustomer(any(AuthenticationRequestDto.class)))
                .thenThrow(new CustomerEntityException("The customer email or phone number already exists"));

        mockMvc.perform(post(AUTH_PATH + AUTHENTICATE)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value(ControllerDetailMsg.EMAIL_MOBILE_EXISTS));

        verify(authenticationService, times(1)).authenticateCustomer(any(AuthenticationRequestDto.class));
    }

    @SneakyThrows
    @Test
    void authenticateCustomer_validCustomerDetails_success() {
        final String jwtToken = "jwt-token";
        final var authResponseDto = AuthenticationResponseDto.builder()
                .token(jwtToken)
                .build();

        when(authenticationService.authenticateCustomer(any(AuthenticationRequestDto.class))).thenReturn(authResponseDto);

        mockMvc.perform(post(AUTH_PATH + AUTHENTICATE)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TestDataGenerator.generateAuthenticationRequestDto())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(jwtToken));
        verify(authenticationService, times(1)).authenticateCustomer(any(AuthenticationRequestDto.class));
    }

    @SneakyThrows
    @Test
    void authenticateCustomer_invalidEmail_throwsUsernameNotFoundException() {
        when(authenticationService.authenticateCustomer(any(AuthenticationRequestDto.class)))
                .thenThrow(new UsernameNotFoundException(ControllerDetailMsg.EMAIL_MOBILE_EXISTS));

        mockMvc.perform(post(AUTH_PATH + AUTHENTICATE)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TestDataGenerator.generateAuthenticationRequestDto())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value(ControllerDetailMsg.USER_NOT_FOUND));
        verify(authenticationService, times(1)).authenticateCustomer(any(AuthenticationRequestDto.class));
    }

    @SneakyThrows
    @Test
    void authenticateCustomer_invalidPassword_throwsAuthenticationException() {
        when(authenticationService.authenticateCustomer(any(AuthenticationRequestDto.class)))
                .thenThrow(new BadCredentialsException(ControllerDetailMsg.USER_NOT_AUTHORIZED));

        mockMvc.perform(post(AUTH_PATH + AUTHENTICATE)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TestDataGenerator.generateAuthenticationRequestDto())))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.detail").value(ControllerDetailMsg.USER_NOT_AUTHORIZED));
        verify(authenticationService, times(1)).authenticateCustomer(any(AuthenticationRequestDto.class));
    }
}
