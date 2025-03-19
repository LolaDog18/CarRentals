package com.woozy.carrentals.bdd.config;

import com.woozy.carrentals.config.Log4j2TestConfig;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@CucumberContextConfiguration
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {BddTestConfig.class, Log4j2TestConfig.class}
)
@ActiveProfiles("integration")
public class CucumberContextConfig {
}