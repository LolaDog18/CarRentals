package com.woozy.carrentals.ui.controller;

import com.woozy.carrentals.constants.Endpoints;
import com.woozy.carrentals.service.AuthenticationService;
import com.woozy.carrentals.shared.dto.request.authentication.AuthenticationRequestDto;
import com.woozy.carrentals.shared.dto.request.customer.RegisterCustomerRequestDto;
import com.woozy.carrentals.shared.dto.response.authentication.AuthenticationResponseDto;
import com.woozy.carrentals.shared.dto.response.customer.CustomerResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Endpoints.AUTH)
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authService;

    @PostMapping("/register-customer")
    public ResponseEntity<CustomerResponseDto> registerCustomer(@RequestBody RegisterCustomerRequestDto userDetails) {
        return ResponseEntity.ok(authService.registerCustomer(userDetails));
    }

    @SneakyThrows
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseDto> authenticateUser(@RequestBody AuthenticationRequestDto authenticationRequestDto) {
        return ResponseEntity.ok(authService.authenticateCustomer(authenticationRequestDto));
    }
}
