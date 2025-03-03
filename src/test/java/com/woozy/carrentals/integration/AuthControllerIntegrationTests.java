package com.woozy.carrentals.integration;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.core.options.Constants.*;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("bdd")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.woozy.carrentals.bdd")
@ConfigurationParameter(key = PLUGIN_PUBLISH_QUIET_PROPERTY_NAME, value = "true")
@ConfigurationParameter(key = FILTER_TAGS_PROPERTY_NAME, value = "(@RunMe or @Cleanup) and not @Skip")
public class AuthControllerIntegrationTests {

}