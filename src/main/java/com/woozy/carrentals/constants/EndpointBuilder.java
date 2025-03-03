package com.woozy.carrentals.constants;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
@NoArgsConstructor
public class EndpointBuilder {
    @Value("${server.servlet.context-path")
    private String basePath;

    public String buildAuthEndpointFor(String authEndpointAction) {
        return buildAuthPath().concat(EndpointResourcePath.AUTH_PATH).concat(authEndpointAction);
    }

    public String buildCustomerEndpointFor(String pathVariable) {
        return buildCustomersPath().concat(pathVariable);
    }

    public String buildAuthPath() {
        return basePath.concat(EndpointResourcePath.AUTH_PATH);
    }

    public String buildCustomersPath() {
        return basePath.concat(EndpointResourcePath.CUSTOMERS_PATH);
    }
}