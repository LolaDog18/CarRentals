package com.woozy.carrentals.controller;

import com.woozy.carrentals.config.Log4j2TestConfig;
import com.woozy.carrentals.security.WebSecurity;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = "server.servlet.context-path=/api/v1")
@AutoConfigureMockMvc(addFilters = false)
@Import({WebSecurity.class, Log4j2TestConfig.class})
public abstract class ControllerTestBase {
}