package com.reloadly.accountmicroservice.helpers;

import com.github.javafaker.Faker;
import com.reloadly.accountmicroservice.dto.request.AccountDto;
import com.reloadly.accountmicroservice.dto.response.AccountMicroServiceResponse;
import com.reloadly.accountmicroservice.models.Account;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static com.reloadly.accountmicroservice.enums.ResponseCode.INTERNAL_SERVER_ERROR;
import static com.reloadly.accountmicroservice.enums.ResponseCode.OK;

public class TestHelper {

    public static Account getCreatedAccount() {
        Faker faker = Faker.instance();
        Account account = Account.builder()
                .email(faker.name().username().concat("@example.name"))
                .firstName(faker.name().firstName())
                .surname(faker.name().lastName())
                .otherName(faker.name().nameWithMiddle())
                .password("pass")
                .phoneNumber(faker.phoneNumber().phoneNumber())
                .active(Boolean.TRUE)
                .build();
        account.setId(1l);

        return account;
    }

    public static Account createAccountRequest() {
        Faker faker = Faker.instance();
        Account account = Account.builder()
                .email(faker.name().username().concat("@example.name"))
                .firstName(faker.name().firstName())
                .surname(faker.name().lastName())
                .otherName(faker.name().nameWithMiddle())
                .password("pass")
                .phoneNumber(faker.phoneNumber().phoneNumber())
                .active(Boolean.TRUE)
                .build();
        account.setId(1l);

        return account;
    }

    public static AccountDto getAccountDto() {
        Faker faker = Faker.instance();
        AccountDto account = AccountDto.builder()
                .email(faker.name().username().concat("@example.name"))
                .firstName(faker.name().firstName())
                .surname(faker.name().lastName())
                .otherName(faker.name().nameWithMiddle())
                .password("pass")
                .phoneNumber(faker.phoneNumber().phoneNumber())
                .build();
        return account;
    }

    public static Mono<AccountMicroServiceResponse> getSuccessfulResponse() {
        AccountMicroServiceResponse response = AccountMicroServiceResponse
                .builder()
                .statusCode(OK.getCanonicalCode())
                .statusMessage(OK.getDescription())
                .timestamp(LocalDateTime.now().toString())
                .data(getCreatedAccount())
                .build();
        return Mono.just(response);
    }

    public static Mono<AccountMicroServiceResponse> getFailureResponse() {
        AccountMicroServiceResponse response = AccountMicroServiceResponse
                .builder()
                .statusCode(INTERNAL_SERVER_ERROR.getCanonicalCode())
                .statusMessage(INTERNAL_SERVER_ERROR.getDescription())
                .timestamp(LocalDateTime.now().toString())
                .data(getCreatedAccount())
                .build();
        return Mono.just(response);
    }
}
