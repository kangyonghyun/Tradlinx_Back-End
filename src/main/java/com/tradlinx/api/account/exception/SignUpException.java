package com.tradlinx.api.account.exception;

public class SignUpException extends RuntimeException {

    private static final String DUPLICATION_ID = "이미 있는 ID 입니다.";
    public SignUpException() {
        super(DUPLICATION_ID);
    }

}
