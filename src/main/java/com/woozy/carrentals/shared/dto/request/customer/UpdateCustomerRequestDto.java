package com.woozy.carrentals.shared.dto.request.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCustomerRequestDto {
    private String email;
    private String mobileNumber;
    private String drivingLicense;
    private String address;
}
