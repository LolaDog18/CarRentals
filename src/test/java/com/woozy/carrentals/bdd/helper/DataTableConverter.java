package com.woozy.carrentals.bdd.helper;

import com.woozy.RegisterCustomerRequestDto;
import io.cucumber.java.DataTableType;

import java.util.Map;

public class DataTableConverter {

    @DataTableType(replaceWithEmptyString = "[blank]")
    public RegisterCustomerRequestDto convertRegisterCustomerDto(Map<String, String> row) {
        return extractRegCustomerDto(row);
    }

    private RegisterCustomerRequestDto extractRegCustomerDto(Map<String, String> row) {
        return RegisterCustomerRequestDto.builder()
                .address(row.get("address"))
                .age(Integer.valueOf(row.get("age")))
                .drivingLicense(row.get("drivingLicense"))
                .email(row.get("email"))
                .firstName(row.get("firstName"))
                .lastName(row.get("lastName"))
                .mobileNumber(row.get("mobileNumber"))
                .password(row.get("password"))
                .build();
    }
}