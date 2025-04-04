package com.woozy.carrentals.integration;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.SelectClasspathResource;

import static io.cucumber.core.options.Constants.FILTER_TAGS_PROPERTY_NAME;
import static io.cucumber.core.options.Constants.PLUGIN_PROPERTY_NAME;

@SelectClasspathResource("bdd/auth")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value =
        "pretty, html:target/cucumber-reports/AuthServiceIT.html, json:target/cucumber-reports/AuthServiceIT.json")
@ConfigurationParameter(key = FILTER_TAGS_PROPERTY_NAME, value = "(@Auth or @Cleanup) or (@Reg or @Cleanup) or @Reg or @Auth or not @Skip")
public class AuthServiceIT extends BaseTestCucumber {

}