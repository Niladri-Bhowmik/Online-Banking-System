package com.banking.online_banking_system.repository;

import com.banking.online_banking_system.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findByUserId(Long userId);

    Account findByAccountNumber(String accountNumber);
}
