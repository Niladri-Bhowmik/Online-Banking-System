package com.banking.online_banking_system.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Account from which money is debited (nullable for deposits)
    @Column(name = "source_account_id")
    private Long sourceAccountId;

    // Account to which money is credited (nullable for withdrawals)
    @Column(name = "destination_account_id")
    private Long destinationAccountId;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private String transactionType; 
    // Example values: DEPOSIT, WITHDRAWAL, TRANSFER

    @Column(nullable = false)
    private LocalDateTime transactionDate;

    @Column(nullable = false)
    private String status; 
    // Example values: SUCCESS, FAILED, PENDING
}
