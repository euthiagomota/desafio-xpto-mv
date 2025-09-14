package com.xpto.financemanager.repositories;

import com.xpto.financemanager.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountIdIn(List<Long> accountIds);

    boolean existsByAccountId(Long accountId);

    List<Transaction> findByAccountIdInAndTransactionDateBetween(
            List<Long> accountIds, LocalDateTime startDate, LocalDateTime endDate
    );
}