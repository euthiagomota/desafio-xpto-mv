package com.xpto.financemanager.dtos;

import java.math.BigDecimal;

public record ResponseAccountDto(
        Long id,
        String accountNumber,
        String bank,
        String agency,
        BigDecimal balance,
        String accountOwner,
        Boolean active
) {
}
