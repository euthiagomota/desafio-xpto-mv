package com.xpto.financemanager.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RequestTransactionDto(
        String accountNumber,
        String bank,
        String agency,
        BigDecimal amount,
        String description
) {
}
