package com.example.CarRentals;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class CarRentalsApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarRentalsApplication.class, args);
    }
}
