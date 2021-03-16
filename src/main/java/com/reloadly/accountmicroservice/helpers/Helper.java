package com.reloadly.accountmicroservice.helpers;

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
}
