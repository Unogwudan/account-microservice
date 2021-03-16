package com.reloadly.accountmicroservice.exception;


import com.reloadly.accountmicroservice.exception.types.AccountMicroServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.util.WebUtils;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            AccountMicroServiceException.class,
            IllegalArgumentException.class,
            MethodArgumentNotValidException.class
    })
    public final ResponseEntity<AccountMicroServiceException> handleException(Exception ex, WebRequest request) {

        var headers = new HttpHeaders();

        if (ex instanceof IllegalArgumentException) {
            var illegalArgumentException = (IllegalArgumentException) ex;

            return handleIllegalArgumentException(illegalArgumentException, headers, request);
        } else if (ex instanceof AccountMicroServiceException) {
            var accountMicroServiceException = (AccountMicroServiceException) ex;

            return handleAccountMicroServiceException(
                    accountMicroServiceException,
                    accountMicroServiceException.getHttpStatus(),
                    headers,
                    request);
        } else {
            return handleExceptionInternal(ex, null, headers, HttpStatus.INTERNAL_SERVER_ERROR, request);
        }
    }

    private ResponseEntity<AccountMicroServiceException> handleIllegalArgumentException(
            IllegalArgumentException ex, HttpHeaders headers, WebRequest request) {
        var apiError = new AccountMicroServiceException();

                apiError.setTimeStamp(getLocalZonedDateTime());
                apiError.setStatusCode(String.valueOf(HttpStatus.NOT_FOUND.value()));
                apiError.setStatusMessage(HttpStatus.NOT_FOUND.name());

        return handleExceptionInternal(ex, apiError, headers, HttpStatus.BAD_REQUEST, request);
    }


    private ResponseEntity<AccountMicroServiceException> handleAccountMicroServiceException(
            AccountMicroServiceException ex,
            HttpStatus httpStatus,
            HttpHeaders headers,
            WebRequest request) {
        var apiError = new AccountMicroServiceException();


        apiError.setTimeStamp(getLocalZonedDateTime());
        apiError.setStatusCode(ex.getStatusCode());
        apiError.setStatusMessage(ex.getStatusMessage());
        apiError.setTransactionId(ex.getTransactionId());

        return handleExceptionInternal(ex, apiError, headers, httpStatus, request);
    }

    protected ResponseEntity<AccountMicroServiceException> handleExceptionInternal(
            Exception ex, AccountMicroServiceException body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, SCOPE_REQUEST);
        }

        return new ResponseEntity<>(body, headers, status);
    }

    private String getLocalZonedDateTime() {
        var dateTime = ZonedDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return dateTime.format(formatter);
    }

}
