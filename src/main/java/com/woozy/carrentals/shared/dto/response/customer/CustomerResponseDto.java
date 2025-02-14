package com.woozy.carrentals.shared.dto.response.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponseDto {
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String mobileNumber;
    private String address;
}
