package com.woozy.carrentals.bdd.config;

import com.woozy.RegisterCustomerRequestDto;
import com.woozy.UpdateCustomerRequestDto;
import com.woozy.car_rentals.response.ClientResponse;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class ScenarioContextAspect {

    private final ScenarioContext scenarioContext;

    /**
     * Automatically stores method return values of specific types in the TestContext.
     */
    @AfterReturning(
            pointcut = "execution(* com.woozy.carrentals.bdd.step_definitions..*(..))",
            returning = "result"
    )
    public void storeResponseInContext(Object result) {
        if (result instanceof ClientResponse) {
            scenarioContext.set(ContextItem.RESPONSE, result);
        } else if (result instanceof RegisterCustomerRequestDto) {
            scenarioContext.set(ContextItem.REGISTER_CUSTOMER_DTO, result);
        } else if (result instanceof UpdateCustomerRequestDto) {
            scenarioContext.set(ContextItem.UPDATE_CUSTOMER_DTO, result);
        }
    }
}

