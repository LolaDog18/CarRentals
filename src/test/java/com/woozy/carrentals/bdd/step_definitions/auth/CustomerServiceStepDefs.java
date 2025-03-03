package com.woozy.carrentals.bdd.step_definitions.auth;

import com.woozy.car_rentals.clients.AuthenticationClient;
import com.woozy.carrentals.service.CustomerService;
import com.woozy.carrentals.shared.dto.response.customer.CustomerResponseDto;
import com.woozy.carrentals.utils.TestDataGenerator;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

@RequiredArgsConstructor
public class CustomerServiceStepDefs {
    private final CustomerService customerService;
    @Lazy
    @Autowired
    private AuthenticationClient authenticationClient;

    private String customerId;

    @Before
    public void saveCustomer() {
        var registerCustomerRequestDto = TestDataGenerator.generateRegisterCustomerRequest();
        customerId = authenticationClient.registerCustomer(registerCustomerRequestDto)
                .extractBodyAs(CustomerResponseDto.class)
                .getUserId();
    }

    @Given("I have existing user id")
    public void iHaveExistingUserId() {
    }

    @When("I call GET \\/customers passing user id")
    public void iCallGETCustomersPassingUserId() {
    }

    @And("body should contain customer details for provided customer id")
    public void bodyShouldContainCustomerDetailsForProvidedCustomerId() {
    }
}