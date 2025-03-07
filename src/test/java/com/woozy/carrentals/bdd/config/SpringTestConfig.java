package com.woozy.carrentals.bdd.config;

import com.woozy.car_rentals.clients.implementation.AuthRestAssuredClientImpl;
import com.woozy.car_rentals.clients.implementation.CustomerServiceRestAssuredClientImpl;
import com.woozy.car_rentals.clients.interfaces.AuthenticationClient;
import com.woozy.car_rentals.clients.interfaces.CustomerServiceClient;
import com.woozy.carrentals.utils.SysPropertyUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Lazy;

@Lazy
@TestConfiguration
@ComponentScan(basePackages = "com.woozy.carrentals")
@EnableAspectJAutoProxy
@EnableAutoConfiguration
public class SpringTestConfig {
    @Value("${server.address:localhost}")
    private String serverAddress;

    @LocalServerPort
    private int port;

    @Value("${server.servlet.context-path}")
    private String basePath;

    @PostConstruct
    private void initializeBaseEndpoint() {
        SysPropertyUtils.setBaseUri(serverAddress, port);
        System.setProperty("basePath", basePath);
    }

    @Bean
    @Lazy
    public AuthenticationClient authenticationClient() {
        return new AuthRestAssuredClientImpl();
    }

    @Bean
    @Lazy
    public CustomerServiceClient customerServiceClient() {
        return new CustomerServiceRestAssuredClientImpl(authenticationClient());
    }
}
