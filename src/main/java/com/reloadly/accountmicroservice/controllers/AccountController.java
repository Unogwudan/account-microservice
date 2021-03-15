package com.reloadly.accountmicroservice.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Slf4j
@RestController
public class AccountController {

    @GetMapping("/accounts")
    public Flux<String> getAccounts() {
        
        return Flux.just("Account A, ", "Account B");
    }

}
