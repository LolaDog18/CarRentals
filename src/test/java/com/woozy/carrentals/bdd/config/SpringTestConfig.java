package com.woozy.carrentals.bdd.config;

import com.woozy.car_rentals.clients.AuthenticationClient;
import com.woozy.car_rentals.clients.AuthenticationClientImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.util.UriComponentsBuilder;

@Lazy
@TestConfiguration
@ComponentScan(basePackages = "com.woozy.carrentals")
@EnableAutoConfiguration
public class SpringTestConfig {
    @Value("${server.address:localhost}")
    private String serverAddress;

    @LocalServerPort
    private int port;

    @Value("${server.servlet.context-path}")
    private String basePath;

    @Bean
    public AuthenticationClient authenticationClient() {
        String baseUri = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(serverAddress)
                .port(port)
                .toUriString();
        return new AuthenticationClientImpl(baseUri, basePath);
    }
}
