package com.xpto.financemanager.dtos;

import com.xpto.financemanager.enuns.ETransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ResponseTransactionDto(
        Long id,
        BigDecimal amount,
        String description,
        LocalDateTime date,
        String customerName,
        ETransactionType transactionType,
        String bank
) {
}
