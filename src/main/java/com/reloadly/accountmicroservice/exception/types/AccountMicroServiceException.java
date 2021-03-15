package com.reloadly.accountmicroservice.exception.types;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountMicroServiceException extends RuntimeException {
    private String statusCode;
    private String statusMessage;
    private String supportMessage;
    private String transactionId;
    private String timeStamp;
    @JsonIgnore
    private HttpStatus httpStatus;

    public AccountMicroServiceException(String statusCode, String statusMessage, String supportMessage, String transactionId, HttpStatus httpStatus) {
        super(statusMessage);
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.supportMessage = supportMessage;
        this.transactionId = transactionId;
        this.httpStatus = httpStatus;
    }
}
