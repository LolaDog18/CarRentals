package com.woozy.carrentals.bdd.step_definitions.auth;

import com.woozy.car_rentals.response.ClientResponse;
import com.woozy.carrentals.bdd.config.ScenarioContext;
import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;

public class CommonVerificationStepDefs {
    @Then("I should receive response with status code of {int}")
    public void iShouldReceiveResponseWithStatusCodeOf(int statusCode) {
        var response = (ClientResponse) ScenarioContext.get("response");
        Assertions.assertEquals(statusCode, response.getStatusCode());
    }
}