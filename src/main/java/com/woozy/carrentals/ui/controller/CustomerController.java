package com.woozy.carrentals.ui.controller;

import com.woozy.UpdateCustomerRequestDto;
import com.woozy.carrentals.constants.EndpointPathVariable;
import com.woozy.carrentals.service.CustomerService;
import com.woozy.carrentals.shared.dto.response.customer.CustomerResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.woozy.carrentals.constants.EndpointResourcePath.CUSTOMERS_PATH;

@RestController
@RequestMapping(CUSTOMERS_PATH)
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping(EndpointPathVariable.USER_ID)
    public ResponseEntity<CustomerResponseDto> getCustomer(@PathVariable String userId) {
        return ResponseEntity.ok(customerService.getCustomer(userId));
    }

    @PutMapping(EndpointPathVariable.USER_ID)
    public ResponseEntity<CustomerResponseDto> updateCustomer(@PathVariable String userId, @RequestBody UpdateCustomerRequestDto userDetails) {
        return ResponseEntity.ok(customerService.updateCustomer(userId, userDetails));
    }

    @DeleteMapping(EndpointPathVariable.USER_ID)
    public ResponseEntity<Void> deleteCustomer(@PathVariable String userId) {
        customerService.deleteCustomer(userId);
        return ResponseEntity.noContent().build();
    }
}
