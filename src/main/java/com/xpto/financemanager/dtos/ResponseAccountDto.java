package com.xpto.financemanager.dtos;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ResponseAccountDto {
    private Long id;
    private  String accountNumber;
    private String bank;
    private String agency;
    private BigDecimal balance;
    private  String accountOwner;
    private  Boolean active;
}
