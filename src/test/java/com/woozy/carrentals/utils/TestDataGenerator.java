package com.woozy.carrentals.utils;

import com.woozy.AuthenticationRequestDto;
import com.woozy.RegisterCustomerRequestDto;
import com.woozy.UpdateCustomerRequestDto;
import com.woozy.carrentals.io.entity.CustomerEntity;
import com.woozy.carrentals.shared.dto.response.customer.CustomerResponseDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.datafaker.Faker;

import java.util.Locale;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestDataGenerator {
    private static final Faker faker = new Faker(Locale.of("pl-PL"));

    public static RegisterCustomerRequestDto generateRegisterCustomerRequest() {
        return RegisterCustomerRequestDto.builder()
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .email(faker.internet().emailAddress())
                .password(faker.internet().password())
                .address(faker.address().fullAddress())
                .age(faker.number().numberBetween(21, 65))
                .drivingLicense(faker.idNumber().valid())
                .mobileNumber(generatePhoneNumber())
                .build();
    }

    public static CustomerEntity generateCustomerEntity() {
        return CustomerEntity.builder()
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .email(faker.internet().emailAddress())
                .password(faker.internet().password())
                .address(faker.address().fullAddress())
                .mobileNumber(generatePhoneNumber())
                .age(faker.number().numberBetween(21, 65))
                .drivingLicense(faker.idNumber().valid())
                .build();
    }

    public static UpdateCustomerRequestDto generateUpdateCustomerRequestDto() {
        return UpdateCustomerRequestDto.builder()
                .email(faker.internet().emailAddress())
                .address(faker.address().fullAddress())
                .drivingLicense(faker.idNumber().valid())
                .mobileNumber(generatePhoneNumber())
                .build();
    }

    public static CustomerResponseDto generateCustomerResponse() {
        return CustomerResponseDto.builder()
                .userId(faker.idNumber().valid())
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .email(faker.internet().emailAddress())
                .mobileNumber(generatePhoneNumber())
                .address(faker.address().fullAddress())
                .build();
    }

    public static AuthenticationRequestDto generateAuthenticationRequestDto() {
        return AuthenticationRequestDto.builder()
                .email(faker.internet().emailAddress())
                .password(faker.internet().password())
                .build();
    }

    public static String generatePhoneNumber() {
        String phoneNumber = faker.phoneNumber().phoneNumberInternational();
        return phoneNumber.replace(" ", "");
    }
}
