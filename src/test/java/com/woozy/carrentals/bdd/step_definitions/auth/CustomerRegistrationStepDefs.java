package com.woozy.carrentals.bdd.step_definitions.auth;

import com.woozy.RegisterCustomerRequestDto;
import com.woozy.car_rentals.clients.interfaces.AuthenticationClient;
import com.woozy.car_rentals.response.ClientResponse;
import com.woozy.carrentals.bdd.config.ContextItem;
import com.woozy.carrentals.bdd.config.ScenarioContext;
import com.woozy.carrentals.shared.dto.response.customer.CustomerResponseDto;
import com.woozy.carrentals.utils.BeanHelperUtils;
import com.woozy.carrentals.utils.ListUtils;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.ProblemDetail;

import java.util.List;

@RequiredArgsConstructor
public class CustomerRegistrationStepDefs {
    private final AuthenticationClient client;
    private final ScenarioContext context;
    private int indexOfSelectedObj;

    @Given("the following customer valid details:")
    public RegisterCustomerRequestDto followingCustomerValidDetails(List<RegisterCustomerRequestDto> customers) {
        return assignRegisterDtoFromCustomerDatatable(customers);
    }

    @Given("the following customer invalid details:")
    public RegisterCustomerRequestDto followingCustomerInvalidDetails(List<RegisterCustomerRequestDto> customers) {
        return assignRegisterDtoFromCustomerDatatable(customers);
    }

    @When("client calls POST \\/register-customer with customer details")
    public ClientResponse clientCallsPOSTRegisterCustomerWithCustomerDetails() {
        var registerCustomerDto = context.get(ContextItem.REGISTER_CUSTOMER_DTO, RegisterCustomerRequestDto.class);
        return client.registerCustomer(registerCustomerDto);
    }

    @When("client calls POST \\/register-customer with the same email as existing one in database")
    public ClientResponse clientCallsPOSTRegisterCustomerWithTheSameEmailAsExistingOneInDatabase() {
        var registerCustomerDto = context.get(ContextItem.REGISTER_CUSTOMER_DTO, RegisterCustomerRequestDto.class);
        return client.registerCustomer(registerCustomerDto);
    }

    @And("body contains saved customer details with non-null values")
    public void bodyContainsSavedCustomerDetailsWithNonNullValues() {
        var registerCustomerDto = context.get(ContextItem.REGISTER_CUSTOMER_DTO, RegisterCustomerRequestDto.class);
        var response = context.get(ContextItem.RESPONSE, ClientResponse.class);
        var responseBody = response.extractBodyAs(CustomerResponseDto.class);
        Assertions.assertFalse(BeanHelperUtils.isNullPropertyExist(responseBody), "Customer registration response contains null fields");
        Assertions.assertAll(
                () -> Assertions.assertEquals(registerCustomerDto.getEmail(), responseBody.getEmail()),
                () -> Assertions.assertEquals(registerCustomerDto.getFirstName(), responseBody.getFirstName()),
                () -> Assertions.assertEquals(registerCustomerDto.getLastName(), responseBody.getLastName()),
                () -> Assertions.assertEquals(registerCustomerDto.getMobileNumber(), responseBody.getMobileNumber()),
                () -> Assertions.assertEquals(registerCustomerDto.getAddress(), responseBody.getAddress())
        );
    }

    @And("body contains the following error message:")
    public void bodyContainsErrorMessage(List<String> errors) {
        var response = context.get(ContextItem.RESPONSE, ClientResponse.class);
        Assertions.assertEquals(errors.get(indexOfSelectedObj), response.extractBodyAs(ProblemDetail.class).getDetail(),
                "Actual and expected body error message mismatch");
    }

    @And("error message in body contains {string}")
    public void errorMessageInBodyContains(String error) {
        var response = context.get(ContextItem.RESPONSE, ClientResponse.class);
        Assertions.assertEquals(error, response.extractBodyAs(ProblemDetail.class).getDetail());
    }

    private RegisterCustomerRequestDto assignRegisterDtoFromCustomerDatatable(List<RegisterCustomerRequestDto> customers) {
        var registerCustomerRequestDto = ListUtils.getRandomElement(customers);
        indexOfSelectedObj = customers.indexOf(registerCustomerRequestDto);
        return registerCustomerRequestDto;
    }
}
