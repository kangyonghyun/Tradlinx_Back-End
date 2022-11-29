package com.tradlinx.api.account.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AccountExceptionController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SignUpException.class)
    public ErrorResult signUpExceptionExHandler(SignUpException e) {
        return new ErrorResult("BAD", e.getMessage());
    }

}
