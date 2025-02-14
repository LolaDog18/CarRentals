package com.woozy.carrentals.service;

import com.woozy.carrentals.shared.dto.request.authentication.AuthenticationRequestDto;
import com.woozy.carrentals.shared.dto.request.customer.RegisterCustomerRequestDto;
import com.woozy.carrentals.shared.dto.response.authentication.AuthenticationResponseDto;
import com.woozy.carrentals.shared.dto.response.customer.CustomerResponseDto;

public interface AuthenticationService {
    CustomerResponseDto registerCustomer(RegisterCustomerRequestDto userDetails);

    AuthenticationResponseDto authenticateCustomer(AuthenticationRequestDto authenticationRequestDto) throws Exception;
}
