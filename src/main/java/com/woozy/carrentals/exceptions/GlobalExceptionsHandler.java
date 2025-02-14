package com.woozy.carrentals.exceptions;

import com.woozy.carrentals.exceptions.errormessages.ControllerDetailMsg;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.net.URI;

import static com.woozy.carrentals.exceptions.errormessages.ControllerDetailMsg.USER_NOT_AUTHORIZED;
import static com.woozy.carrentals.exceptions.errormessages.ControllerDetailMsg.USER_NOT_FOUND;

@ControllerAdvice
public class GlobalExceptionsHandler {
    @ExceptionHandler(UsernameNotFoundException.class)
    public ProblemDetail handleUsernameNotFoundException(UsernameNotFoundException e, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        problemDetail.setType(URI.create("https://example.com/not-found"));
        problemDetail.setDetail(USER_NOT_FOUND);
        problemDetail.setInstance(URI.create(request.getDescription(false)));
        return problemDetail;
    }

    @ExceptionHandler(AuthenticationException.class)
    public ProblemDetail handleAuthenticationException(AuthenticationException e, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, e.getMessage());
        problemDetail.setType(URI.create("https://example.com/unauthorized"));
        problemDetail.setDetail(USER_NOT_AUTHORIZED);
        problemDetail.setInstance(URI.create(request.getDescription(false)));
        return problemDetail;
    }

    @ExceptionHandler(CustomerEntityException.class)
    public ProblemDetail handleCustomerEntityException(CustomerEntityException e, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        problemDetail.setType(URI.create("https://example.com/bad-request"));
        problemDetail.setDetail(ControllerDetailMsg.EMAIL_MOBILE_EXISTS);
        problemDetail.setInstance(URI.create(request.getDescription(false)));
        return problemDetail;
    }
}
