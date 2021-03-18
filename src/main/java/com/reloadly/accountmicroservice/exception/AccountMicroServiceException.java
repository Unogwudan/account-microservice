package com.reloadly.accountmicroservice.exception;

import lombok.Data;


@Data
public class AccountMicroServiceException extends RuntimeException {
    private final Integer httpCode;
    private String statusCode;

    public AccountMicroServiceException(Integer httpCode, String message, String statusCode) {
        super(message);
        this.httpCode = httpCode;
        this.statusCode = statusCode;
    }
}