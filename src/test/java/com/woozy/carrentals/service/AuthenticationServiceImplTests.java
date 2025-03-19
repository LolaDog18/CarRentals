package com.woozy.carrentals.service;

import com.woozy.AuthenticationRequestDto;
import com.woozy.RegisterCustomerRequestDto;
import com.woozy.carrentals.config.Log4j2TestConfig;
import com.woozy.carrentals.exceptions.CustomerEntityException;
import com.woozy.carrentals.io.entity.CustomerEntity;
import com.woozy.carrentals.io.entity.Role;
import com.woozy.carrentals.repository.CustomerRepository;
import com.woozy.carrentals.security.JwtService;
import com.woozy.carrentals.service.impl.AuthenticationServiceImpl;
import com.woozy.carrentals.shared.dto.response.customer.CustomerResponseDto;
import com.woozy.carrentals.utils.TestDataGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.woozy.carrentals.exceptions.errormessages.ServiceErrMsg.*;
import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Import(Log4j2TestConfig.class)
public class AuthenticationServiceImplTests {
    @InjectMocks
    private AuthenticationServiceImpl authenticationService;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtService jwtService;
    private RegisterCustomerRequestDto registerRequest;

    @BeforeEach
    void setUp() {
        registerRequest = TestDataGenerator.generateRegisterCustomerRequest();
    }

    @Test
    void registerCustomer_validDetails_returnsCustomerResponse() {
        when(customerRepository.save(any(CustomerEntity.class))).thenAnswer(
                invocation -> {
                    CustomerEntity entity = invocation.getArgument(0);
                    entity.setUserId(UUID.randomUUID().toString());
                    return entity;
                }
        );

        CustomerResponseDto response = authenticationService.registerCustomer(registerRequest);

        assertNotNull(response);
        assertNotNull(response.getUserId());
        assertAll(
                () -> assertEquals(registerRequest.getFirstName(), response.getFirstName()),
                () -> assertEquals(registerRequest.getLastName(), response.getLastName()),
                () -> assertEquals(registerRequest.getEmail(), response.getEmail())
        );

        verify(customerRepository, times(1)).save(any(CustomerEntity.class));
    }

    @Test
    void registerCustomer_duplicateEmail_throwsException() {
        CustomerEntity customerEntity = CustomerEntity.builder()
                .userId(UUID.randomUUID().toString())
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .mobileNumber(registerRequest.getMobileNumber())
                .address(registerRequest.getAddress())
                .age(registerRequest.getAge())
                .drivingLicense(registerRequest.getDrivingLicense())
                .build();

        when(customerRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.of(customerEntity));

        CustomerEntityException exception = assertThrows(CustomerEntityException.class, () -> authenticationService.registerCustomer(registerRequest));
        String expectedMessage = format(CUSTOMER_WITH_EMAIL_EXISTS, registerRequest.getEmail());
        assertEquals(expectedMessage, exception.getMessage());
        verify(customerRepository, never()).save(any(CustomerEntity.class));
    }

    @Test
    void registerCustomer_duplicateMobileNumber_throwsException() {
        var customerEntity = new CustomerEntity();
        when(customerRepository.findByMobileNumber(registerRequest.getMobileNumber())).thenReturn(Optional.of(customerEntity));

        CustomerEntityException exception = assertThrows(CustomerEntityException.class, () -> authenticationService.registerCustomer(registerRequest));
        String expectedMessage = format(CUSTOMER_WITH_MOBILE_EXISTS, registerRequest.getMobileNumber());
        assertEquals(expectedMessage, exception.getMessage());
        verify(customerRepository, never()).save(any(CustomerEntity.class));
    }

    @Test
    void authenticateCustomer_validCredentials_returnsAuthenticationResponse() {
        var authenticationRequest = AuthenticationRequestDto.builder()
                .email(registerRequest.getEmail())
                .password(registerRequest.getPassword())
                .build();

        final var customerEntity = new CustomerEntity();

        final String jwtToken = "jwt-token";

        Authentication authentication = new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),
                authenticationRequest.getPassword(),
                List.of(new SimpleGrantedAuthority(Role.CUSTOMER.name())));

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(customerRepository.findByEmail(authenticationRequest.getEmail())).thenReturn(Optional.of(customerEntity));
        when(jwtService.generateToken(customerEntity)).thenReturn(jwtToken);

        var response = authenticationService.authenticateCustomer(authenticationRequest);
        assertNotNull(response);
        assertEquals(jwtToken, response.getToken());
    }

    @Test
    void authenticateCustomer_invalidEmail_throwsUsernameNotFoundException() {
        var authenticationRequest = AuthenticationRequestDto.builder()
                .email(registerRequest.getEmail())
                .password(registerRequest.getPassword())
                .build();

        when(customerRepository.findByEmail(authenticationRequest.getEmail())).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> authenticationService.authenticateCustomer(authenticationRequest));
        String expectedMessage = String.format(CUSTOMER_WITH_EMAIL_NOT_FOUND, authenticationRequest.getEmail());
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void authenticateCustomer_invalidCredentials_throwsException() {
        var authenticationRequest = AuthenticationRequestDto.builder()
                .email(registerRequest.getEmail())
                .password(registerRequest.getPassword())
                .build();
        var customerEntity = new CustomerEntity();

        when(customerRepository.findByEmail(authenticationRequest.getEmail())).thenReturn(Optional.of(customerEntity));
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException(BAD_CREDENTIALS));

        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> authenticationService.authenticateCustomer(authenticationRequest));
        assertEquals(BAD_CREDENTIALS, exception.getMessage());
    }
}