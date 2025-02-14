package com.woozy.carrentals.ui.controller;

import com.woozy.carrentals.constants.Endpoints;
import com.woozy.carrentals.service.CustomerService;
import com.woozy.carrentals.shared.dto.request.customer.UpdateCustomerRequestDto;
import com.woozy.carrentals.shared.dto.response.customer.CustomerResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Endpoints.CUSTOMERS)
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping("/{userId}")
    public ResponseEntity<CustomerResponseDto> getCustomer(@PathVariable String userId) {
        return ResponseEntity.ok(customerService.getCustomer(userId));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<CustomerResponseDto> updateCustomer(@PathVariable String userId, @RequestBody UpdateCustomerRequestDto userDetails) {
        return ResponseEntity.ok(customerService.updateCustomer(userId, userDetails));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable String userId) {
        customerService.deleteCustomer(userId);
        return ResponseEntity.noContent().build();
    }
}
