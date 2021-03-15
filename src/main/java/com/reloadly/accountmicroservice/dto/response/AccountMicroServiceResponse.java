package com.reloadly.accountmicroservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.reloadly.accountmicroservice.models.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountMicroServiceResponse {
    private String statusCode;
    private String statusMessage;
    private String timestamp;
    private Account data;
}
