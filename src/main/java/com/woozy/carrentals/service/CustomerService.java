package com.woozy.carrentals.service;

import com.woozy.UpdateCustomerRequestDto;
import com.woozy.carrentals.shared.dto.response.customer.CustomerResponseDto;

public interface CustomerService {
    CustomerResponseDto updateCustomer(String userId, UpdateCustomerRequestDto userDetails);

    CustomerResponseDto getCustomer(String userId);

    void deleteCustomer(String userId);
}
