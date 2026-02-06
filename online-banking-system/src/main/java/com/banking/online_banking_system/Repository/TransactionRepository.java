package com.banking.online_banking_system.repository;

import com.banking.online_banking_system.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findBySourceAccountId(Long sourceAccountId);

    List<Transaction> findByDestinationAccountId(Long destinationAccountId);
}
