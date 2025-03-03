package com.woozy.carrentals.bdd.hooks;

import com.woozy.carrentals.bdd.config.ScenarioContext;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

@Log4j2
public class CucumberHooks {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @After("@Cleanup")
    @Transactional
    public void databaseCleanup(Scenario scenario) {
        log.info("Cleaning up database for: {}", scenario.getName());

        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE"); // Disable foreign key checks
        jdbcTemplate.execute("TRUNCATE TABLE users");
        jdbcTemplate.execute("TRUNCATE TABLE roles");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE"); // Re-enable foreign key checks
    }

    @After
    public void cleanUpStepDefContext(Scenario scenario) {
        log.info("Cleaning up context for step definition {}", scenario.getName());
        if (ScenarioContext.getContextSize() != 0)
            ScenarioContext.clear();
    }
}