package com.woozy.carrentals.bdd.helper;

import com.woozy.RegisterCustomerRequestDto;
import com.woozy.UpdateCustomerRequestDto;
import io.cucumber.java.DataTableType;

import java.util.Map;

public class DataTableConverter {

    @DataTableType(replaceWithEmptyString = "[blank]")
    public RegisterCustomerRequestDto convertRegisterCustomerDto(Map<String, String> row) {
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

    @DataTableType
    public UpdateCustomerRequestDto convertUpdateCustomerDto(Map<String, String> row) {
        return UpdateCustomerRequestDto.builder()
                .address(row.get("address"))
                .email(row.get("email"))
                .drivingLicense(row.get("drivingLicense"))
                .mobileNumber(row.get("mobileNumber"))
                .build();
    }
}