package com.woozy.carrentals.exceptions;

import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;
import com.woozy.carrentals.exceptions.errormessages.ControllerDetailMsg;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.woozy.carrentals.exceptions.errormessages.ControllerDetailMsg.USER_NOT_AUTHORIZED;
import static com.woozy.carrentals.exceptions.errormessages.ControllerDetailMsg.USER_NOT_FOUND;

@ControllerAdvice
public class GlobalExceptionsHandler {
    @ExceptionHandler(UsernameNotFoundException.class)
    public ProblemDetail handleUsernameNotFoundException(UsernameNotFoundException e, WebRequest request) {
        ProblemDetail problemDetail = setBaseProblemDetailFor(HttpStatus.BAD_REQUEST, request, e);
        problemDetail.setDetail(USER_NOT_FOUND);
        return problemDetail;
    }

    @ExceptionHandler(AuthenticationException.class)
    public ProblemDetail handleAuthenticationException(AuthenticationException e, WebRequest request) {
        ProblemDetail problemDetail = setBaseProblemDetailFor(HttpStatus.UNAUTHORIZED, request, e);
        problemDetail.setDetail(USER_NOT_AUTHORIZED);
        return problemDetail;
    }

    @ExceptionHandler(CustomerEntityException.class)
    public ProblemDetail handleCustomerEntityException(CustomerEntityException e, WebRequest request) {
        ProblemDetail problemDetail = setBaseProblemDetailFor(HttpStatus.BAD_REQUEST, request, e);
        problemDetail.setDetail(ControllerDetailMsg.EMAIL_MOBILE_EXISTS);
        return problemDetail;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstrainViolationException(ConstraintViolationException e, WebRequest request) {
        ProblemDetail problemDetail = setBaseProblemDetailFor(HttpStatus.BAD_REQUEST, request, e);
        problemDetail.setDetail(e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", ")));
        return problemDetail;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException e, WebRequest request) {
        ProblemDetail problemDetail = setBaseProblemDetailFor(HttpStatus.BAD_REQUEST, request, e);
        String detail = e.getBindingResult().getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("and "));

        problemDetail.setDetail(detail);
        return problemDetail;
    }

    @ExceptionHandler(MysqlDataTruncation.class)
    public ProblemDetail handleMysqlDataTruncationException(MysqlDataTruncation e, WebRequest request) {
        ProblemDetail problemDetail = setBaseProblemDetailFor(HttpStatus.BAD_REQUEST, request, e);
        problemDetail.setDetail(e.getMessage());
        return problemDetail;
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ProblemDetail handleCustomerNotFoundException(CustomerNotFoundException e, WebRequest request) {
        ProblemDetail problemDetail = setBaseProblemDetailFor(HttpStatus.BAD_REQUEST, request, e);
        problemDetail.setDetail(USER_NOT_FOUND);
        return problemDetail;
    }

    private ProblemDetail setBaseProblemDetailFor(HttpStatus httpStatus, WebRequest webRequest, Exception ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(httpStatus, ex.getMessage());
        problemDetail.setType(httpStatusURIMap().get(httpStatus));
        problemDetail.setInstance(URI.create(webRequest.getDescription(false)));
        return problemDetail;
    }

    private Map<HttpStatus, URI> httpStatusURIMap() {
        Map<HttpStatus, URI> httpStatusURIMap = new HashMap<>();
        httpStatusURIMap.put(HttpStatus.BAD_REQUEST, URI.create("https://example.com/bad-request"));
        httpStatusURIMap.put(HttpStatus.UNAUTHORIZED, URI.create("https://example.com/unauthorized"));
        return httpStatusURIMap;
    }
}
