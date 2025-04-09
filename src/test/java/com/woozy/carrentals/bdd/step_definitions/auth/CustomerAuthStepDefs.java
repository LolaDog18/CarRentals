package com.woozy.carrentals.bdd.step_definitions.auth;

import com.woozy.AuthenticationRequestDto;
import com.woozy.RegisterCustomerRequestDto;
import com.woozy.car_rentals.clients.interfaces.AuthenticationClient;
import com.woozy.car_rentals.response.ClientResponse;
import com.woozy.carrentals.bdd.config.ContextItem;
import com.woozy.carrentals.bdd.config.ScenarioContext;
import com.woozy.carrentals.shared.dto.response.authentication.AuthenticationResponseDto;
import com.woozy.carrentals.utils.ListUtils;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.platform.commons.util.StringUtils;

import java.util.List;

import static org.apache.http.HttpStatus.SC_OK;

@RequiredArgsConstructor
public class CustomerAuthStepDefs {
    private final AuthenticationClient client;
    private final ScenarioContext context;

    @Given("I register a customer through POST \\/register-customer with following customer details:")
    public void iRegisterACustomerThroughPOSTRegisterCustomerWithFollowingCustomerDetails(List<RegisterCustomerRequestDto> customers) {
        var registerCustomerRequestDto = ListUtils.getRandomElement(customers);
        var clientResponse = client.registerCustomer(registerCustomerRequestDto);
        Assertions.assertEquals(SC_OK, clientResponse.getStatusCode());
    }

    @When("I call POST \\/authenticate endpoint with {email} and {password}")
    public ClientResponse iCallPOSTAuthenticateEndpointWithEmailAndPassword(String email, String password) {
        return client.authenticateCustomer(new AuthenticationRequestDto(email, password));
    }

    @And("body should contain authentication token")
    public void bodyShouldContainAuthenticationToken() {
        var response = context.get(ContextItem.RESPONSE, ClientResponse.class);
        String responseToken = response.extractBodyAs(AuthenticationResponseDto.class).getToken();
        Assertions.assertFalse(StringUtils.isBlank(responseToken));
    }

    @Given("I don't register a customer")
    public void iDonTRegisterACustomer() {
    }
}
