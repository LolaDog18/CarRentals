package com.woozy.carrentals.exceptions.errormessages;


public abstract class ServiceErrMsg {
    public static final String CUSTOMER_WITH_EMAIL_EXISTS = "Customer with email %s already exists";
    public static final String CUSTOMER_WITH_MOBILE_EXISTS = "Customer with mobile number %s already exists";
    public static final String CUSTOMER_WITH_EMAIL_NOT_FOUND = "Customer with email %s not found";
    public static final String BAD_CREDENTIALS = "Bad credentials";
    public static final String CUSTOMER_ID_NOT_FOUND = "Customer with id %s not found";
}
