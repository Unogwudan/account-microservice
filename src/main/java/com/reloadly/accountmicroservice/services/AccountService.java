package com.reloadly.accountmicroservice.services;

import com.reloadly.accountmicroservice.auth.PBKDF2Encoder;
import com.reloadly.accountmicroservice.auth.User;
import com.reloadly.accountmicroservice.dto.request.AccountRequest;
import com.reloadly.accountmicroservice.dto.response.AccountMicroServiceResponse;
import com.reloadly.accountmicroservice.enums.Role;
import com.reloadly.accountmicroservice.helpers.Helper;
import com.reloadly.accountmicroservice.models.Account;
import com.reloadly.accountmicroservice.repositories.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

import static com.reloadly.accountmicroservice.enums.ResponseCode.*;

@Slf4j
@Service
public class AccountService {

    @Value("${notification.service.endpoint}")
    private String notificationEndpoint;
    private final PBKDF2Encoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final WebClientHttpService httpService;

    public AccountService(PBKDF2Encoder passwordEncoder, AccountRepository accountRepository, WebClientHttpService httpService) {
        this.passwordEncoder = passwordEncoder;
        this.accountRepository = accountRepository;
        this.httpService = httpService;
    }

    /**
     * Create an account
     * @param accountRequest the dto containing the account info
     * @return {@link Mono<AccountMicroServiceResponse>}
     */
    public Mono<AccountMicroServiceResponse> createAccount(AccountRequest accountRequest) {

        AccountMicroServiceResponse response;
        Account account = accountRepository.findByEmail(accountRequest.getEmail());

        if (!Objects.isNull(account)) {
            return Mono.just(new AccountMicroServiceResponse(ALREADY_EXIST.getCanonicalCode(), ALREADY_EXIST.getDescription(), LocalDateTime.now().toString(), account));
        }

        Account savedAccount = Account.builder()
                .email(accountRequest.getEmail())
                .firstName(accountRequest.getFirstName())
                .surname(accountRequest.getSurname())
                .otherName(accountRequest.getOtherName())
                .password(passwordEncoder.encode(accountRequest.getPassword()))
                .phoneNumber(accountRequest.getPhoneNumber())
                .role(Role.ROLE_USER)
                .build();

        try {
            response = new AccountMicroServiceResponse(OK.getCanonicalCode(), OK.getDescription(), LocalDateTime.now().toString(), accountRepository.saveAndFlush(savedAccount));
        } catch (Exception e) {
            log.error("Exception occurred while creating account {}", e.getMessage());
            return Mono.just(new AccountMicroServiceResponse(INTERNAL_SERVER_ERROR.getCanonicalCode(), INTERNAL_SERVER_ERROR.getDescription(), LocalDateTime.now().toString(), null));
        }

        httpService.post(notificationEndpoint, Helper.buildEmailRequest(accountRequest), AccountMicroServiceResponse.class)
                .subscribeOn(Schedulers.elastic()).subscribe(res -> log.info("Email Notification Response {}", res));

        return Mono.just(response);
    }

    /**
     * Update an account
     * @param accountRequest the dto containing the details to update
     * @return {@link Mono<AccountMicroServiceResponse>}
     */
    public Mono<AccountMicroServiceResponse> updateAccount(long id, AccountRequest accountRequest) {

        AccountMicroServiceResponse response;
        Account account = accountRepository.findById(id);

        if (Objects.isNull(account)) {
            return Mono.just(new AccountMicroServiceResponse(NOT_FOUND.getCanonicalCode(), NOT_FOUND.getDescription(), LocalDateTime.now().toString(), account));
        }

        account.setEmail(accountRequest.getEmail());
        account.setFirstName(accountRequest.getFirstName());
        account.setSurname(accountRequest.getSurname());
        account.setOtherName(accountRequest.getOtherName());
        account.setPassword(accountRequest.getPassword());
        account.setPhoneNumber(accountRequest.getPhoneNumber());

        try {
            response = new AccountMicroServiceResponse(OK.getCanonicalCode(), OK.getDescription(), LocalDateTime.now().toString(),  accountRepository.saveAndFlush(account));
            log.error("Account updated successfully {}", account);
        } catch (Exception e) {
            log.error("Exception occurred while updating account {}", e.getMessage());
            response = new AccountMicroServiceResponse(INTERNAL_SERVER_ERROR.getCanonicalCode(), INTERNAL_SERVER_ERROR.getDescription(), LocalDateTime.now().toString(), account);
        }
        return Mono.just(response);
    }

    /**
     * Find an account by the email address supplied
     * @param email to search for
     * @return {@link Mono<AccountMicroServiceResponse>}
     */
    public Mono<AccountMicroServiceResponse> findAccountByEmail(String email) {
        Account account = accountRepository.findByEmail(email);
        if (Objects.isNull(account)) {
            return Mono.just(new AccountMicroServiceResponse(NOT_FOUND.getCanonicalCode(), NOT_FOUND.getDescription(), LocalDateTime.now().toString(), account));
        }

        return Mono.just(new AccountMicroServiceResponse(OK.getCanonicalCode(), OK.getDescription(), LocalDateTime.now().toString(), account));
    }

    /**
     * Find an account by email and return the User Details Object
     * @param username of the user
     * @return {@link Mono<User>}
     */
    public Mono<User> findByUsername(String username) {
        Account account = accountRepository.findByEmail(username);

        if (!Objects.isNull(account)) {
            User user = new User(account.getId(), account.getEmail(), account.getPassword(), Arrays.asList(account.getRole()), account.getFirstName(),
                    account.getSurname(), account.getPhoneNumber());
            return Mono.just(user);
        } else {
            return Mono.empty();
        }
    }

}
