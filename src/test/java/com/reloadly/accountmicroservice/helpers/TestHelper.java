package com.reloadly.accountmicroservice.helpers;

import com.github.javafaker.Faker;
import com.reloadly.accountmicroservice.auth.User;
import com.reloadly.accountmicroservice.dto.request.AccountDto;
import com.reloadly.accountmicroservice.dto.request.AuthRequest;
import com.reloadly.accountmicroservice.dto.response.AccountMicroServiceResponse;
import com.reloadly.accountmicroservice.enums.Role;
import com.reloadly.accountmicroservice.models.Account;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Arrays;

import static com.reloadly.accountmicroservice.enums.ResponseCode.INTERNAL_SERVER_ERROR;
import static com.reloadly.accountmicroservice.enums.ResponseCode.OK;

public class TestHelper {

    public static String TEST_EMAIL = "unogwudan@gmail.com";

    public static Account getCreatedAccount() {
        Faker faker = Faker.instance();
        Account account = Account.builder()
                .email(faker.name().username().concat("@example.name"))
                .firstName(faker.name().firstName())
                .surname(faker.name().lastName())
                .otherName(faker.name().nameWithMiddle())
                .password("pass")
                .phoneNumber(faker.phoneNumber().phoneNumber())
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
                .build();

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

    public static AuthRequest authRequest() {
        return new AuthRequest("unogwudan@example.com", "password");
    }

    public static Mono<User> getUser() {
        return Mono.just(new User("unogwudan@example.com", "password", Arrays.asList(Role.ROLE_USER)));
    }
}
