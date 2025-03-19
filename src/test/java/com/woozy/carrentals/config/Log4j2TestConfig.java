package com.woozy.carrentals.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.core.config.Configurator;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.core.env.Environment;

@Log4j2
@TestConfiguration
public class Log4j2TestConfig {
    private final Environment env;

    public Log4j2TestConfig(Environment env) {
        this.env = env;
    }

    @PostConstruct
    public void setUpLog4j2ForTests() {
        String activeProfile = env.getActiveProfiles().length > 0 ? env.getActiveProfiles()[0] : "default";
        String logConfigPath;

        if ("integration".equals(activeProfile)) {
            logConfigPath = "src/test/resources/log4j2-integration.xml";
        } else {
            logConfigPath = "src/test/resources/log4j2-unit.xml";
        }

        log.info("Loading Log4j2 configuration: {}", logConfigPath);
        Configurator.initialize(null, logConfigPath);
    }
}