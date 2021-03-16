package com.reloadly.accountmicroservice.controllers;

import com.reloadly.accountmicroservice.auth.JWTUtil;
import com.reloadly.accountmicroservice.auth.PBKDF2Encoder;
import com.reloadly.accountmicroservice.constants.CommonConstants;
import com.reloadly.accountmicroservice.dto.request.AuthRequest;
import com.reloadly.accountmicroservice.dto.response.AccountMicroServiceResponse;
import com.reloadly.accountmicroservice.dto.response.AuthResponse;
import com.reloadly.accountmicroservice.helpers.Helper;
import com.reloadly.accountmicroservice.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(CommonConstants.API_VERSION + "auth")
public class AuthenticationController {

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private PBKDF2Encoder passwordEncoder;

    @Autowired
    private AccountService accountService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<AccountMicroServiceResponse>> login(@RequestBody AuthRequest request) {
        return accountService.findByUsername(request.getUsername()).map((userDetails) -> {
            if (passwordEncoder.encode(request.getPassword()).equals(userDetails.getPassword())) {
                return ResponseEntity.ok(Helper.buildSuccessfulResponse(new AuthResponse(jwtUtil.generateToken(userDetails))));
            } else {
                return ResponseEntity.ok(Helper.buildFailureResponse(request));
            }
        }).defaultIfEmpty(ResponseEntity.ok(Helper.buildFailureResponse(request)));
    }
}
