package com.woozy.carrentals.ui.controller;

import com.woozy.AuthenticationRequestDto;
import com.woozy.RegisterCustomerRequestDto;
import com.woozy.carrentals.constants.AuthEndpointAction;
import com.woozy.carrentals.service.AuthenticationService;
import com.woozy.carrentals.shared.dto.response.authentication.AuthenticationResponseDto;
import com.woozy.carrentals.shared.dto.response.customer.CustomerResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.woozy.carrentals.constants.EndpointResourcePath.AUTH_PATH;

@RestController
@RequestMapping(AUTH_PATH)
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authService;

    @PostMapping(AuthEndpointAction.REGISTER_CUSTOMER)
    public ResponseEntity<CustomerResponseDto> registerCustomer(@Valid @RequestBody RegisterCustomerRequestDto userDetails) {
        return ResponseEntity.ok(authService.registerCustomer(userDetails));
    }

    @SneakyThrows
    @PostMapping(AuthEndpointAction.AUTHENTICATE)
    public ResponseEntity<AuthenticationResponseDto> authenticateUser(@RequestBody AuthenticationRequestDto authenticationRequestDto) {
        return ResponseEntity.ok(authService.authenticateCustomer(authenticationRequestDto));
    }
}
