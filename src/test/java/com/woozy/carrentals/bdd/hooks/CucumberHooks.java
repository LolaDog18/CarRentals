package com.woozy.carrentals.bdd.hooks;

import com.woozy.carrentals.bdd.config.ScenarioContext;
import com.woozy.carrentals.bdd.helper.AttachmentsHelper;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

@Log4j2
public class CucumberHooks {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    ScenarioContext context;

    @After("@Cleanup")
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
        if (context.getContextSize() != 0)
            context.clear();
    }

    @After
    public void attachLogs(Scenario scenario) {
        if (scenario.isFailed()) {
            log.info("Attaching test logs to the report...");
            AttachmentsHelper.attachLogToReport(scenario, "logs/test-layer.log");
        }
    }
}