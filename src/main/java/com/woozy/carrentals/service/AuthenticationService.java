package com.woozy.carrentals.service;

import com.woozy.AuthenticationRequestDto;
import com.woozy.RegisterCustomerRequestDto;
import com.woozy.carrentals.shared.dto.response.authentication.AuthenticationResponseDto;
import com.woozy.carrentals.shared.dto.response.customer.CustomerResponseDto;

public interface AuthenticationService {
    CustomerResponseDto registerCustomer(RegisterCustomerRequestDto userDetails);

    AuthenticationResponseDto authenticateCustomer(AuthenticationRequestDto authenticationRequestDto) throws Exception;
}
