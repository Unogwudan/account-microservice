package com.reloadly.accountmicroservice.repositories;

import com.reloadly.accountmicroservice.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByEmail(String email);
    Account findById(long id);
}
