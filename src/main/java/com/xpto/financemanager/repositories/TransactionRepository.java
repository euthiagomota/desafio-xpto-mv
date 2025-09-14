package com.xpto.financemanager.repositories;

import com.xpto.financemanager.entities.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    List<TransactionEntity> findByAccountIdIn(List<Long> accountIds);

    boolean existsByAccountId(Long accountId);

    List<TransactionEntity> findByAccountIdInAndTransactionDateBetween(
            List<Long> accountIds, LocalDateTime startDate, LocalDateTime endDate
    );
}