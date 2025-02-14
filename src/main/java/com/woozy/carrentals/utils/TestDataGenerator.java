package com.woozy.carrentals.utils;

import com.woozy.carrentals.io.entity.CustomerEntity;
import com.woozy.carrentals.shared.dto.request.authentication.AuthenticationRequestDto;
import com.woozy.carrentals.shared.dto.request.customer.RegisterCustomerRequestDto;
import com.woozy.carrentals.shared.dto.request.customer.UpdateCustomerRequestDto;
import com.woozy.carrentals.shared.dto.response.customer.CustomerResponseDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.datafaker.Faker;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestDataGenerator {
    private static final Faker faker = new Faker();

    public static RegisterCustomerRequestDto generateRegisterCustomerRequest() {
        return RegisterCustomerRequestDto.builder()
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .email(faker.internet().emailAddress())
                .password(faker.internet().password())
                .address(faker.address().fullAddress())
                .age(faker.number().numberBetween(21, 65))
                .drivingLicense(faker.idNumber().valid())
                .mobileNumber(faker.phoneNumber().cellPhone())
                .build();
    }

    public static CustomerEntity generateCustomerEntity() {
        return CustomerEntity.builder()
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .email(faker.internet().emailAddress())
                .password(faker.internet().password())
                .address(faker.address().fullAddress())
                .mobileNumber(faker.phoneNumber().cellPhone())
                .age(faker.number().numberBetween(21, 65))
                .drivingLicense(faker.idNumber().valid())
                .build();
    }

    public static UpdateCustomerRequestDto generateUpdateCustomerRequestDto() {
        return UpdateCustomerRequestDto.builder()
                .email(faker.internet().emailAddress())
                .address(faker.address().fullAddress())
                .drivingLicense(faker.idNumber().valid())
                .mobileNumber(faker.phoneNumber().cellPhone())
                .build();
    }

    public static CustomerResponseDto generateCustomerResponse() {
        return CustomerResponseDto.builder()
                .userId(faker.idNumber().valid())
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .email(faker.internet().emailAddress())
                .mobileNumber(faker.phoneNumber().cellPhone())
                .address(faker.address().fullAddress())
                .build();
    }

    public static AuthenticationRequestDto generateAuthenticationRequestDto() {
        return AuthenticationRequestDto.builder()
                .email(faker.internet().emailAddress())
                .password(faker.internet().password())
                .build();
    }
}
