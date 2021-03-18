package com.reloadly.accountmicroservice.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Optional;

@Getter
public enum ResponseCode {

    OK("0", "0000", "Success", HttpStatus.OK),
    INVALID_CREDENTIALS("1", "1000", "Username or Password Incorrect", HttpStatus.BAD_REQUEST),
    ACCOUNT_NOT_ACTIVE("126", "1200","Account not active", HttpStatus.BAD_REQUEST),
    ALREADY_EXIST("137", "4001","Account already exist", HttpStatus.FORBIDDEN),
    UNMARSHALL_EXCEPTION("170", "1700", "Json conversion failed", HttpStatus.EXPECTATION_FAILED),
    NOT_FOUND("139", "3004","Account not found", HttpStatus.NOT_FOUND),
    OUT_OF_BOUNDS("264", "3002","The result of the operation would be a nominal value that is out of bounds.", HttpStatus.BAD_REQUEST),
    PERMISSION_DENIED("266", "4000","Permission denied", HttpStatus.UNAUTHORIZED),
    BAD_REQUEST("400", "4000", "Invalid request", HttpStatus.BAD_REQUEST),
    UNAUTHORISED("401", "4001", "You are not authorized to access this resource", HttpStatus.UNAUTHORIZED),
    OTHER_ERROR_NO_RETRY("999", "3001","Other Error No Retry", HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR("500", "3001", "There was an error while processing the request.", HttpStatus.INTERNAL_SERVER_ERROR);

    ResponseCode(
            final String errorCode, final String canonicalCode,
            final String description, final HttpStatus httpStatus
    ) {
        this.errorCode = errorCode;
        this.canonicalCode = canonicalCode;
        this.description = description;
        this.httpStatus = httpStatus;
    }

    private String errorCode;
    private String canonicalCode;
    private String description;
    private HttpStatus httpStatus;

    public static Optional<ResponseCode> getResponseCode(String code) {
        for(ResponseCode canonicalErrorCode : ResponseCode.values()) {
            if(canonicalErrorCode.errorCode.equals(code)) {
                return Optional.of(canonicalErrorCode);
            }
        }
        return Optional.empty();
    }
}
