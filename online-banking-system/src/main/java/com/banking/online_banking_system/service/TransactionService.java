package com.banking.online_banking_system.service;

import com.banking.online_banking_system.model.Account;
import com.banking.online_banking_system.model.Transaction;
import com.banking.online_banking_system.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TransactionService {

    private final AccountService accountService;
    private final TransactionRepository transactionRepository;

    public TransactionService(AccountService accountService,
                              TransactionRepository transactionRepository) {
        this.accountService = accountService;
        this.transactionRepository = transactionRepository;
    }

    /**
     * Transfers money from one account to another.
     * This method is atomic: either everything succeeds or everything rolls back.
     */
    @Transactional
    public void transferMoney(Long sourceAccountId,
                              Long destinationAccountId,
                              BigDecimal amount) {

        // 1. Basic validations (fail fast)
        if (sourceAccountId == null || destinationAccountId == null) {
            throw new IllegalArgumentException("Account IDs must not be null");
        }

        if (sourceAccountId.equals(destinationAccountId)) {
            throw new IllegalArgumentException("Source and destination accounts must be different");
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be greater than zero");
        }

        // 2. Load accounts (managed entities inside transaction)
        Account sourceAccount = accountService.getAccountById(sourceAccountId);
        Account destinationAccount = accountService.getAccountById(destinationAccountId);

        // 3. Business rule: sufficient balance
        if (sourceAccount.getBalance().compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient balance");
        }

        // 4. Update balances
        BigDecimal updatedSourceBalance =
                sourceAccount.getBalance().subtract(amount);

        BigDecimal updatedDestinationBalance =
                destinationAccount.getBalance().add(amount);

        accountService.updateBalance(sourceAccount, updatedSourceBalance);
        accountService.updateBalance(destinationAccount, updatedDestinationBalance);

        // 5. Persist transaction record (audit trail)
        Transaction transaction = new Transaction();
        transaction.setSourceAccountId(sourceAccountId);
        transaction.setDestinationAccountId(destinationAccountId);
        transaction.setAmount(amount);
        transaction.setTimestamp(LocalDateTime.now());

        transactionRepository.save(transaction);
    }
}
