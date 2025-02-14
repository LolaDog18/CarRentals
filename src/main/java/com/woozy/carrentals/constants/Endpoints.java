package com.woozy.carrentals.constants;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Endpoints {
    public static final String API_VERSION = "/api/v1";
    public static final String CUSTOMERS = API_VERSION + "/customers";
    public static final String AUTH = API_VERSION + "/auth";
}
