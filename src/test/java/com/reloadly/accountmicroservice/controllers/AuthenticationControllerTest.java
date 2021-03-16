package com.reloadly.accountmicroservice.controllers;

import com.reloadly.accountmicroservice.AccountMicroServiceApplication;
import com.reloadly.accountmicroservice.auth.PBKDF2Encoder;
import com.reloadly.accountmicroservice.constants.CommonConstants;
import com.reloadly.accountmicroservice.dto.response.AccountMicroServiceResponse;
import com.reloadly.accountmicroservice.helpers.TestHelper;
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

import static com.reloadly.accountmicroservice.enums.ResponseCode.INVALID_CREDENTIALS;
import static com.reloadly.accountmicroservice.enums.ResponseCode.OK;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;


@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = AccountMicroServiceApplication.class)
@AutoConfigureMockMvc
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@PropertySource("classpath:/test.properties")
public class AuthenticationControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private AccountService accountService;

    @MockBean
    private PBKDF2Encoder passwordEncoder;

    @Test
    public void testLoginSuccess() {
        doReturn("password").when(passwordEncoder).encode(any());
        doReturn(TestHelper.getUser()).when(accountService).findByUsername(any());
        webTestClient.post()
                .uri(CommonConstants.API_VERSION + "auth")
                .body(BodyInserters.fromValue(TestHelper.authRequest()))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(AccountMicroServiceResponse.class)
                .value(response -> Assert.assertEquals(response.getStatusCode(), OK.getCanonicalCode()));
    }

    @Test
    public void testLoginFailure() {
        doReturn("pass").when(passwordEncoder).encode(any());
        doReturn(TestHelper.getUser()).when(accountService).findByUsername(any());
        webTestClient.post()
                .uri(CommonConstants.API_VERSION + "auth")
                .body(BodyInserters.fromValue(TestHelper.authRequest()))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(AccountMicroServiceResponse.class)
                .value(response -> Assert.assertEquals(response.getStatusCode(), INVALID_CREDENTIALS.getCanonicalCode()));
    }
}