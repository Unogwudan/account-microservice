package com.reloadly.accountmicroservice.controllers;

import com.reloadly.accountmicroservice.constants.CommonConstants;
import com.reloadly.accountmicroservice.dto.request.AccountRequest;
import com.reloadly.accountmicroservice.dto.response.AccountMicroServiceResponse;
import com.reloadly.accountmicroservice.services.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(CommonConstants.API_VERSION + "accounts")
public class AccountController {

    private AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<AccountMicroServiceResponse>> createAccount(@Valid @RequestBody AccountRequest accountRequest) {
        return accountService.createAccount(accountRequest)
                .map(account -> ResponseEntity.ok(account));
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<AccountMicroServiceResponse>> updateAccount(@Valid @RequestBody AccountRequest accountRequest, @PathVariable long id) {
        return accountService.updateAccount(id, accountRequest)
                .map(account -> ResponseEntity.ok(account));
    }

    @GetMapping("/{email}")
    public Mono<ResponseEntity<AccountMicroServiceResponse>> getAccount(@PathVariable String email) {
        return accountService.findAccountByEmail(email)
                .map(account -> ResponseEntity.ok(account));
    }

}
