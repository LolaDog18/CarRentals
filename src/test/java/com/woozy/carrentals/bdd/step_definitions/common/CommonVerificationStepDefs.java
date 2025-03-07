package com.woozy.carrentals.bdd.step_definitions.common;

import com.woozy.car_rentals.response.ClientResponse;
import com.woozy.carrentals.bdd.config.ContextItem;
import com.woozy.carrentals.bdd.config.ScenarioContext;
import io.cucumber.java.en.Then;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;

@RequiredArgsConstructor
public class CommonVerificationStepDefs {
    private final ScenarioContext context;

    @Then("I should receive response with status code of {int}")
    public void iShouldReceiveResponseWithStatusCodeOf(int statusCode) {
        var response = context.get(ContextItem.RESPONSE, ClientResponse.class);
        Assertions.assertEquals(statusCode, response.getStatusCode());
    }
}