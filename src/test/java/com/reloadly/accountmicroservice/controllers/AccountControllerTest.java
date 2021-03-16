package com.reloadly.accountmicroservice.controllers;

import com.reloadly.accountmicroservice.AccountMicroServiceApplication;
import com.reloadly.accountmicroservice.constants.CommonConstants;
import com.reloadly.accountmicroservice.dto.response.AccountMicroServiceResponse;
import com.reloadly.accountmicroservice.helpers.TestHelper;
import com.reloadly.accountmicroservice.repositories.AccountRepository;
import com.reloadly.accountmicroservice.services.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static com.reloadly.accountmicroservice.enums.ResponseCode.OK;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = AccountMicroServiceApplication.class)
@AutoConfigureMockMvc
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@PropertySource("classpath:test.properties")
public class AccountControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private AccountService accountService;

    @MockBean
    private AccountRepository accountRepository;

    @Test
    public void createAccount() {
        doReturn(TestHelper.getSuccessfulResponse()).when(accountService).createAccount(any());
        webTestClient.post()
                .uri(CommonConstants.API_VERSION + "accounts")
                .body(BodyInserters.fromValue(TestHelper.getAccountDto()))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(AccountMicroServiceResponse.class)
                .value(response -> Assert.assertEquals(response.getStatusCode(), OK.getCanonicalCode()));
    }

    @Test
    public void updateAccount() {
        doReturn(TestHelper.getSuccessfulResponse()).when(accountService).updateAccount(anyLong(), any());
        webTestClient.put()
                .uri(CommonConstants.API_VERSION + "accounts/1")
                .body(BodyInserters.fromValue(TestHelper.getAccountDto()))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(AccountMicroServiceResponse.class)
                .value(response -> Assert.assertEquals(response.getStatusCode(), OK.getCanonicalCode()));
    }
}