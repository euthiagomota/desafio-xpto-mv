package com.xpto.financemanager.dtos;

import java.math.BigDecimal;

public record RequestAccountDto(
        String accountNumber,
        String bank,
        String agency,
        BigDecimal balance
) {}
