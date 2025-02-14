package com.woozy.carrentals.shared.dto.request.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterCustomerRequestDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String mobileNumber;
    private Integer age;
    private String drivingLicense;
    private String address;
}
