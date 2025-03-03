package com.woozy.carrentals.service.impl;

import com.woozy.AuthenticationRequestDto;
import com.woozy.RegisterCustomerRequestDto;
import com.woozy.carrentals.exceptions.CustomerEntityException;
import com.woozy.carrentals.io.entity.CustomerEntity;
import com.woozy.carrentals.io.entity.Role;
import com.woozy.carrentals.repository.CustomerRepository;
import com.woozy.carrentals.security.JwtService;
import com.woozy.carrentals.service.AuthenticationService;
import com.woozy.carrentals.shared.dto.response.authentication.AuthenticationResponseDto;
import com.woozy.carrentals.shared.dto.response.customer.CustomerResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.woozy.carrentals.exceptions.errormessages.ServiceErrMsg.*;
import static java.lang.String.format;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthenticationServiceImpl implements AuthenticationService {
    private final CustomerRepository customerRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public CustomerResponseDto registerCustomer(RegisterCustomerRequestDto userDetails) {
        if (customerRepository.findByEmail(userDetails.getEmail()).isPresent()) {
            log.error("Customer with email {} already exists", userDetails.getEmail());
            throw new CustomerEntityException(format(CUSTOMER_WITH_EMAIL_EXISTS, userDetails.getEmail()));
        }

        if (customerRepository.findByMobileNumber(userDetails.getMobileNumber()).isPresent()) {
            log.error("Customer with mobile number {} already exists", userDetails.getMobileNumber());
            throw new CustomerEntityException(format(CUSTOMER_WITH_MOBILE_EXISTS, userDetails.getMobileNumber()));
        }

        var user = CustomerEntity.builder()
                .firstName(userDetails.getFirstName())
                .lastName(userDetails.getLastName())
                .email(userDetails.getEmail())
                .password(bCryptPasswordEncoder.encode(userDetails.getPassword()))
                .mobileNumber(userDetails.getMobileNumber())
                .age(userDetails.getAge())
                .drivingLicense(userDetails.getDrivingLicense())
                .emailVerificationToken("")
                .emailVerificationStatus(false)
                .address(userDetails.getAddress())
                .roles(List.of(Role.CUSTOMER))
                .build();

        customerRepository.save(user);

        return CustomerResponseDto.builder()
                .userId(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .mobileNumber(userDetails.getMobileNumber())
                .address(userDetails.getAddress())
                .build();
    }

    @Override
    public AuthenticationResponseDto authenticateCustomer(AuthenticationRequestDto authenticationRequestDto) {
        var user = customerRepository.findByEmail(authenticationRequestDto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException(format(CUSTOMER_WITH_EMAIL_NOT_FOUND, authenticationRequestDto.getEmail())));

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequestDto.getEmail(), authenticationRequestDto.getPassword()));
        } catch (AuthenticationException ex) {
            log.warn("Invalid credentials for customer with email {}", authenticationRequestDto.getEmail());
        }

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequestDto.getEmail(), authenticationRequestDto.getPassword()));
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponseDto.builder()
                .token(jwtToken)
                .build();
    }
}
