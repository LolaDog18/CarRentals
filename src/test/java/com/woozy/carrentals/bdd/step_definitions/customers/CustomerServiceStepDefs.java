package com.woozy.carrentals.bdd.step_definitions.customers;

import com.woozy.RegisterCustomerRequestDto;
import com.woozy.UpdateCustomerRequestDto;
import com.woozy.car_rentals.clients.interfaces.CustomerServiceClient;
import com.woozy.car_rentals.constants.AuthMode;
import com.woozy.car_rentals.response.ClientResponse;
import com.woozy.carrentals.bdd.config.ContextItem;
import com.woozy.carrentals.bdd.config.ScenarioContext;
import com.woozy.carrentals.io.entity.CustomerEntity;
import com.woozy.carrentals.repository.CustomerRepository;
import com.woozy.carrentals.service.AuthenticationService;
import com.woozy.carrentals.shared.dto.response.customer.CustomerResponseDto;
import com.woozy.carrentals.utils.CsvReaderUtils;
import com.woozy.carrentals.utils.ListUtils;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.springframework.context.annotation.Lazy;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@Lazy
@RequiredArgsConstructor
public class CustomerServiceStepDefs {
    private final ScenarioContext context;
    private final CustomerServiceClient client;
    private final AuthenticationService authService;
    private final CustomerRepository customerRepository;
    private String customerId;
    private RegisterCustomerRequestDto registerCustomerDto;

    @Before(order = 1, value = "@Customers")
    public void readCustomerDataAndSaveToDb() {
        List<RegisterCustomerRequestDto> registerCustomerRequestDtos = CsvReaderUtils.loadObjectList(RegisterCustomerRequestDto.class, "register_customer_dto.csv");
        registerCustomerDto = ListUtils.getRandomElement(registerCustomerRequestDtos);
        log.info("Inserting customer into db...");
        customerId = authService.registerCustomer(registerCustomerDto).getUserId();
    }

    @Before(order = 2, value = "@Customers")
    public void setUpAuthenticationMode() {
        log.info("Setting up authentication mode with register customer credentials");
        client.setAuthMode(AuthMode.VALID(registerCustomerDto.getEmail(), registerCustomerDto.getPassword()));
    }

    @Given("the following customer details for update:")
    public UpdateCustomerRequestDto followingCustomerDetailsForUpdate(List<UpdateCustomerRequestDto> updatedCustomerDetails) {
        return ListUtils.getRandomElement(updatedCustomerDetails);
    }

    @When("I call GET \\/customers passing existing customer userId")
    public ClientResponse iCallGETCustomersPassingUserId() {
        return client.getCustomer(customerId);
    }

    @And("body should contain customer details for provided customer userId")
    public void bodyShouldContainCustomerDetailsForProvidedCustomerId() {
        var response = context.get(ContextItem.RESPONSE, ClientResponse.class);
        var customerResponseDto = response.extractBodyAs(CustomerResponseDto.class);

        Assertions.assertAll(
                () -> Assertions.assertEquals(customerId, customerResponseDto.getUserId()),
                () -> Assertions.assertEquals(registerCustomerDto.getEmail(), customerResponseDto.getEmail()),
                () -> Assertions.assertEquals(registerCustomerDto.getFirstName(), customerResponseDto.getFirstName()),
                () -> Assertions.assertEquals(registerCustomerDto.getLastName(), customerResponseDto.getLastName()),
                () -> Assertions.assertEquals(registerCustomerDto.getMobileNumber(), customerResponseDto.getMobileNumber()),
                () -> Assertions.assertEquals(registerCustomerDto.getAddress(), customerResponseDto.getAddress())
        );
    }

    @When("I call GET \\/customers passing non-existing customer userId")
    public ClientResponse iCallGETCustomersPassingNonExistingCustomerUserId() {
        var customerId = UUID.randomUUID().toString();
        return client.getCustomer(customerId);
    }

    @When("I call DELETE \\/customers passing existing userId")
    public ClientResponse iCallDELETECustomersPassingExistingUserId() {
        return client.deleteCustomer(customerId);
    }

    @And("customer should be removed from database")
    public void customerShouldBeRemovedFromDatabase() {
        Optional<CustomerEntity> customerEntity = customerRepository.findByEmail(registerCustomerDto.getEmail());
        Assertions.assertTrue(customerEntity.isEmpty(), "Customer presents in database");
    }

    @When("I call DELETE \\/customers passing non-existing userId")
    public ClientResponse iCallDELETECustomersPassingNonExistingUserId() {
        return client.deleteCustomer(UUID.randomUUID().toString());
    }

    @When("I call PUT \\/customers passing existing userId and customer details for update")
    public ClientResponse iCallPUTCustomersPassingExistingUserIdAndCustomerDetailsForUpdate() {
        var updateCustomerDto = context.get(ContextItem.UPDATE_CUSTOMER_DTO, UpdateCustomerRequestDto.class);
        return client.updateCustomer(customerId, updateCustomerDto);
    }

    @And("body should contain updated customer details")
    public void bodyShouldContainUpdateCustomerDetails() {
        var customerResponseDto = context.get(ContextItem.RESPONSE, ClientResponse.class).extractBodyAs(CustomerResponseDto.class);
        var updateCustomerDto = context.get(ContextItem.UPDATE_CUSTOMER_DTO, UpdateCustomerRequestDto.class);

        Assertions.assertAll(
                () -> Assertions.assertEquals(updateCustomerDto.getEmail(), customerResponseDto.getEmail()),
                () -> Assertions.assertEquals(updateCustomerDto.getAddress(), customerResponseDto.getAddress()),
                () -> Assertions.assertEquals(updateCustomerDto.getMobileNumber(), customerResponseDto.getMobileNumber())
        );
    }

    @And("customer details are updated in database")
    public void customerDetailsAreUpdatedInDatabase() {
        var customerEntity = customerRepository.findByUserId(customerId).orElseThrow();
        var updateCustomerDto = context.get(ContextItem.UPDATE_CUSTOMER_DTO, UpdateCustomerRequestDto.class);
        Assertions.assertAll(
                () -> Assertions.assertEquals(updateCustomerDto.getEmail(), customerEntity.getEmail()),
                () -> Assertions.assertEquals(updateCustomerDto.getAddress(), customerEntity.getAddress()),
                () -> Assertions.assertEquals(updateCustomerDto.getMobileNumber(), customerEntity.getMobileNumber()),
                () -> Assertions.assertEquals(updateCustomerDto.getDrivingLicense(), customerEntity.getDrivingLicense())
        );
    }

    @When("I call PUT \\/customers passing non-existing userId and customer details for update")
    public ClientResponse iCallPUTCustomersPassingNonExistingUserIdAndCustomerDetailsForUpdate() {
        var updateCustomerDto = context.get(ContextItem.UPDATE_CUSTOMER_DTO, UpdateCustomerRequestDto.class);
        return client.updateCustomer(UUID.randomUUID().toString(), updateCustomerDto);
    }
}