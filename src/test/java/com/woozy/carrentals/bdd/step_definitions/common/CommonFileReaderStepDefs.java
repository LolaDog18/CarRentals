package com.woozy.carrentals.bdd.step_definitions.common;

import com.woozy.RegisterCustomerRequestDto;
import com.woozy.carrentals.utils.CsvReaderUtils;
import com.woozy.carrentals.utils.ListUtils;
import io.cucumber.java.en.Given;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CommonFileReaderStepDefs {
    @Given("customer is read from register_customer_dto.csv file")
    public RegisterCustomerRequestDto customerIsReadFromCsvFile() {
        List<RegisterCustomerRequestDto> customerEntities = CsvReaderUtils.loadObjectList(RegisterCustomerRequestDto.class,
                "register_customer_dto.csv");
        return ListUtils.getRandomElement(customerEntities);
    }
}
