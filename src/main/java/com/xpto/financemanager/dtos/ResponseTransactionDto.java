package com.xpto.financemanager.dtos;

import com.xpto.financemanager.enums.TransactionType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseTransactionDto {

    private Long id;
    private BigDecimal amount;
    private String description;
    private LocalDateTime date;
    private String customerName;
    private TransactionType transactionType;
    private String bank;
}
