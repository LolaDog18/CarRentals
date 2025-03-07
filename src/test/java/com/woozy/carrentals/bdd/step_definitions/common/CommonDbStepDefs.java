package com.woozy.carrentals.bdd.step_definitions.common;

import com.woozy.RegisterCustomerRequestDto;
import com.woozy.carrentals.bdd.config.ContextItem;
import com.woozy.carrentals.bdd.config.ScenarioContext;
import com.woozy.carrentals.service.AuthenticationService;
import io.cucumber.java.en.And;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommonDbStepDefs {
    private final AuthenticationService authService;
    private final ScenarioContext context;

    @And("customer is saved to database")
    public void customerIsSavedToDatabase() {
        var registerCustomerDto = context.get(ContextItem.REGISTER_CUSTOMER_DTO, RegisterCustomerRequestDto.class);
        authService.registerCustomer(registerCustomerDto);
    }
}
