package com.reloadly.accountmicroservice.helpers;

import com.reloadly.accountmicroservice.constants.CommonConstants;
import com.reloadly.accountmicroservice.dto.request.AccountRequest;
import com.reloadly.accountmicroservice.dto.request.EmailRequest;
import com.reloadly.accountmicroservice.dto.response.AccountMicroServiceResponse;
import com.reloadly.accountmicroservice.enums.ResponseCode;

import java.time.LocalDateTime;

public class Helper {

    public static AccountMicroServiceResponse buildSuccessfulResponse(Object data) {
        return AccountMicroServiceResponse.builder()
                .statusCode(ResponseCode.OK.getCanonicalCode())
                .statusMessage(ResponseCode.OK.getDescription())
                .timestamp(LocalDateTime.now().toString())
                .data(data)
                .build();

    }

    public static AccountMicroServiceResponse buildFailureResponse(Object data) {
        return AccountMicroServiceResponse.builder()
                .statusCode(ResponseCode.INVALID_CREDENTIALS.getCanonicalCode())
                .statusMessage(ResponseCode.INVALID_CREDENTIALS.getDescription())
                .timestamp(LocalDateTime.now().toString())
                .data(data)
                .build();

    }

    public static EmailRequest buildEmailRequest(AccountRequest request) {
        String message = CommonConstants.ACCOUNT_CREATION_MESSAGE
                .replace("{username}", request.getEmail())
                .replace("{password}", request.getPassword());

        return EmailRequest.builder()
                .to(request.getEmail())
                .subject(CommonConstants.ACCOUNT_CREATION_SUBJECT)
                .message(message)
                .build();
    }
}
