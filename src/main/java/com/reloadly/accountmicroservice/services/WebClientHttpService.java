package com.reloadly.accountmicroservice.services;

import com.reloadly.accountmicroservice.dto.response.AccountMicroServiceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@Service
public class WebClientHttpService {

    @Value("${webclient.requestTimeout}")
    private String requestTimeout;

    public Mono<AccountMicroServiceResponse> post(String endpoint, Object request, Class<AccountMicroServiceResponse> responseClass) {
        return WebClient.create()
                .post()
                .uri(endpoint)
                .body(BodyInserters.fromValue(request))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new HttpServerErrorException(clientResponse.statusCode(), clientResponse.statusCode().getReasonPhrase())))
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new HttpServerErrorException(clientResponse.statusCode(), clientResponse.statusCode().getReasonPhrase())))
                .bodyToMono(AccountMicroServiceResponse.class).timeout(Duration.ofMillis(Integer.valueOf(requestTimeout)));
    }

}
