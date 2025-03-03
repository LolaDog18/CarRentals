package com.woozy.carrentals.bdd.step_definitions.auth;

import com.woozy.RegisterCustomerRequestDto;
import com.woozy.car_rentals.clients.AuthenticationClient;
import com.woozy.car_rentals.response.ClientResponse;
import com.woozy.carrentals.bdd.config.ScenarioContext;
import com.woozy.carrentals.io.entity.CustomerEntity;
import com.woozy.carrentals.io.entity.Role;
import com.woozy.carrentals.repository.CustomerRepository;
import com.woozy.carrentals.shared.dto.response.customer.CustomerResponseDto;
import com.woozy.carrentals.utils.BeanHelperUtils;
import com.woozy.carrentals.utils.CsvReaderUtils;
import com.woozy.carrentals.utils.ListUtils;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ProblemDetail;

import java.util.List;

@RequiredArgsConstructor
public class CustomerRegistrationStepDefs {
    private final AuthenticationClient client;
    @Autowired
    @Lazy
    private CustomerRepository repository;
    private RegisterCustomerRequestDto registerCustomerRequestDto;
    private ClientResponse response;
    private int indexOfSelectedObj;
    private CustomerEntity customerEntity;

    @Given("the following customer valid details:")
    public void followingCustomerValidDetails(List<RegisterCustomerRequestDto> customers) {
        assignRegisterDtoFromCustomerDatatable(customers);
    }

    @Given("the following customer invalid details:")
    public void followingCustomerInvalidDetails(List<RegisterCustomerRequestDto> customers) {
        assignRegisterDtoFromCustomerDatatable(customers);
    }

    @Given("customer is read from customer_entity.csv file")
    public void customerIsReadFromCsvFile() {
        List<CustomerEntity> customerEntities = CsvReaderUtils.loadObjectList(CustomerEntity.class, "customer_entities.csv");
        customerEntity = ListUtils.getRandomElement(customerEntities);
        customerEntity.setRoles(List.of(Role.CUSTOMER));
    }

    @And("customer is saved to database")
    public void customerIsSavedToDatabase() {
        registerCustomerRequestDto = new RegisterCustomerRequestDto();
        BeanHelperUtils.copyProperties(registerCustomerRequestDto, repository.save(customerEntity));
    }

    @When("client calls POST \\/register-customer with customer details")
    public void clientCallsPOSTRegisterCustomerWithCustomerDetails() {
        response = client.registerCustomer(registerCustomerRequestDto);
        ScenarioContext.set("response", response);
    }

    @When("client calls POST \\/register-customer with the same email as existing one in database")
    public void clientCallsPOSTRegisterCustomerWithTheSameEmailAsExistingOneInDatabase() {
        response = client.registerCustomer(registerCustomerRequestDto);
        ScenarioContext.set("response", response);
    }

    @And("body contains saved customer details with non-null values")
    public void bodyContainsSavedCustomerDetailsWithNonNullValues() {
        var responseBody = response.extractBodyAs(CustomerResponseDto.class);
        Assertions.assertFalse(BeanHelperUtils.isNullPropertyExist(responseBody), "Customer registration response contains null fields");
        Assertions.assertAll(
                () -> Assertions.assertEquals(registerCustomerRequestDto.getEmail(), responseBody.getEmail()),
                () -> Assertions.assertEquals(registerCustomerRequestDto.getFirstName(), responseBody.getFirstName()),
                () -> Assertions.assertEquals(registerCustomerRequestDto.getLastName(), responseBody.getLastName()),
                () -> Assertions.assertEquals(registerCustomerRequestDto.getMobileNumber(), responseBody.getMobileNumber()),
                () -> Assertions.assertEquals(registerCustomerRequestDto.getAddress(), responseBody.getAddress())
        );
    }

    @And("body contains the following error message:")
    public void bodyContainsErrorMessage(List<String> errors) {
        Assertions.assertEquals(errors.get(indexOfSelectedObj), response.extractBodyAs(ProblemDetail.class).getDetail(),
                "Actual and expected body error message mismatch");
    }

    @And("error message in body contains {string}")
    public void errorMessageInBodyContains(String error) {
        Assertions.assertEquals(error, response.extractBodyAs(ProblemDetail.class).getDetail());
    }

    private void assignRegisterDtoFromCustomerDatatable(List<RegisterCustomerRequestDto> customers) {
        registerCustomerRequestDto = ListUtils.getRandomElement(customers);
        indexOfSelectedObj = customers.indexOf(registerCustomerRequestDto);
    }
}
