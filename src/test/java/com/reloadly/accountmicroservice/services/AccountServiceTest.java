package com.reloadly.accountmicroservice.services;

import com.reloadly.accountmicroservice.dto.response.AccountMicroServiceResponse;
import com.reloadly.accountmicroservice.helpers.TestHelper;
import com.reloadly.accountmicroservice.repositories.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static com.reloadly.accountmicroservice.enums.ResponseCode.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.powermock.api.mockito.PowerMockito.doReturn;

//import org.junit.jupiter.api.Test;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {AccountService.class})
@EnableConfigurationProperties
@TestPropertySource("classpath:/test.properties")
@Slf4j
public class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @MockBean
    private AccountRepository accountRepository;

    @Test
    public void createAccountSuccess() {

        doReturn(null).when(accountRepository).findByEmail(any());
        doReturn(TestHelper.getCreatedAccount()).when(accountRepository).saveAndFlush(any());

        Mono<AccountMicroServiceResponse> account = accountService.createAccount(TestHelper.getAccountDto());

        StepVerifier
                .create(account)
                .expectNextMatches(response -> response.getStatusCode().equals(OK.getCanonicalCode()))
                .verifyComplete();
    }

    @Test
    public void updateAccountSuccess() {
        doReturn(TestHelper.getCreatedAccount()).when(accountRepository).findById(anyLong());
        doReturn(TestHelper.getCreatedAccount()).when(accountRepository).saveAndFlush(any());

        Mono<AccountMicroServiceResponse> account = accountService.updateAccount(1l, TestHelper.getAccountDto());

        StepVerifier
                .create(account)
                .expectNextMatches(response -> response.getStatusCode().equals(OK.getCanonicalCode()))
                .verifyComplete();
    }

    @Test
    public void createAccountFailure() {

        doReturn(TestHelper.getCreatedAccount()).when(accountRepository).findByEmail(any());
        Mono<AccountMicroServiceResponse> account = accountService.createAccount(TestHelper.getAccountDto());

        StepVerifier
                .create(account)
                .expectNextMatches(response -> response.getStatusCode().equals(ALREADY_EXIST.getCanonicalCode()))
                .verifyComplete();
    }

    @Test
    public void updateAccountFailure() {

        doReturn(null).when(accountRepository).findById(anyLong());
        Mono<AccountMicroServiceResponse> account = accountService.updateAccount(1l, TestHelper.getAccountDto());

        StepVerifier
                .create(account)
                .expectNextMatches(response -> response.getStatusCode().equals(NOT_FOUND.getCanonicalCode()))
                .verifyComplete();
    }
}