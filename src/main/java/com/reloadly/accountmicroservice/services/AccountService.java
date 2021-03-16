package com.reloadly.accountmicroservice.services;

import com.reloadly.accountmicroservice.auth.PBKDF2Encoder;
import com.reloadly.accountmicroservice.auth.User;
import com.reloadly.accountmicroservice.dto.request.AccountDto;
import com.reloadly.accountmicroservice.dto.response.AccountMicroServiceResponse;
import com.reloadly.accountmicroservice.enums.Role;
import com.reloadly.accountmicroservice.models.Account;
import com.reloadly.accountmicroservice.repositories.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

import static com.reloadly.accountmicroservice.enums.ResponseCode.*;

@Slf4j
@Service
public class AccountService {

    private final PBKDF2Encoder passwordEncoder;
    private final AccountRepository accountRepository;

    public AccountService(PBKDF2Encoder passwordEncoder, AccountRepository accountRepository) {
        this.passwordEncoder = passwordEncoder;
        this.accountRepository = accountRepository;
    }

    /**
     * Create an account
     * @param accountDto the dto containing the account info
     * @return {@link Mono<AccountMicroServiceResponse>}
     */
    public Mono<AccountMicroServiceResponse> createAccount(AccountDto accountDto) {

        AccountMicroServiceResponse response;
        Account account = accountRepository.findByEmail(accountDto.getEmail());

        if (!Objects.isNull(account)) {
            return Mono.just(new AccountMicroServiceResponse(ALREADY_EXIST.getCanonicalCode(), ALREADY_EXIST.getDescription(), LocalDateTime.now().toString(), account));
        }

        Account savedAccount = Account.builder()
                .email(accountDto.getEmail())
                .firstName(accountDto.getFirstName())
                .surname(accountDto.getSurname())
                .otherName(accountDto.getOtherName())
                .password(passwordEncoder.encode(accountDto.getPassword()))
                .phoneNumber(accountDto.getPhoneNumber())
                .role(Role.ROLE_USER)
                .build();

        try {
            response = new AccountMicroServiceResponse(OK.getCanonicalCode(), OK.getDescription(), LocalDateTime.now().toString(), accountRepository.saveAndFlush(savedAccount));
        } catch (Exception e) {
            log.error("Exception occurred while creating account {}", e.getMessage());
            response = new AccountMicroServiceResponse(INTERNAL_SERVER_ERROR.getCanonicalCode(), INTERNAL_SERVER_ERROR.getDescription(), LocalDateTime.now().toString(), account);
        }
        return Mono.just(response);
    }

    /**
     * Update an account
     * @param accountDto the dto containing the details to update
     * @return {@link Mono<AccountMicroServiceResponse>}
     */
    public Mono<AccountMicroServiceResponse> updateAccount(long id, AccountDto accountDto) {

        AccountMicroServiceResponse response;
        Account account = accountRepository.findById(id);

        if (Objects.isNull(account)) {
            return Mono.just(new AccountMicroServiceResponse(NOT_FOUND.getCanonicalCode(), NOT_FOUND.getDescription(), LocalDateTime.now().toString(), account));
        }

        account.setEmail(accountDto.getEmail());
        account.setFirstName(accountDto.getFirstName());
        account.setSurname(accountDto.getSurname());
        account.setOtherName(accountDto.getOtherName());
        account.setPassword(accountDto.getPassword());
        account.setPhoneNumber(accountDto.getPhoneNumber());

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
            User user = new User(account.getEmail(), account.getPassword(), Arrays.asList(account.getRole()));
            return Mono.just(user);
        } else {
            return Mono.empty();
        }
    }

}
