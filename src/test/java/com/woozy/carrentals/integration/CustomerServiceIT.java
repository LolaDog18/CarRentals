package com.woozy.carrentals.integration;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.SelectClasspathResource;

import static io.cucumber.core.options.Constants.FILTER_TAGS_PROPERTY_NAME;
import static io.cucumber.core.options.Constants.PLUGIN_PROPERTY_NAME;

@SelectClasspathResource("bdd/customer-service")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value =
        "pretty, html:target/cucumber-reports/CustomerServiceIT.html, json:target/cucumber-reports/CustomerServiceIT.json")
@ConfigurationParameter(key = FILTER_TAGS_PROPERTY_NAME, value = "(@Customers or @Cleanup) and not @Skip")
public class CustomerServiceIT extends BaseTestCucumber {
}
