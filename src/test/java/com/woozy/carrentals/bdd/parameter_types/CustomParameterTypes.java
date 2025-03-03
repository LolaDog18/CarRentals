package com.woozy.carrentals.bdd.parameter_types;

import io.cucumber.java.ParameterType;

public class CustomParameterTypes {
    @ParameterType(".*")
    public String email(String email) {
        return email;
    }

    @ParameterType(".*")
    public String password(String password) {
        return password;
    }
}