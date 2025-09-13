package com.xpto.financemanager.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record RequestTransactionDto(
        @NotBlank(message = "O número da conta não pode estar em branco")
        @Pattern(regexp = "\\d{4,10}", message = "O número da conta deve conter entre 4 e 10 dígitos")
        String accountNumber,

        @NotBlank(message = "O banco não pode estar em branco")
        @Size(min = 2, max = 100, message = "O nome do banco deve ter entre 2 e 100 caracteres")
        String bank,

        @NotBlank(message = "A agência não pode estar em branco")
        @Pattern(regexp = "\\d{3,6}", message = "A agência deve conter entre 3 e 6 dígitos")
        String agency,

        @NotBlank(message = "A transação deve ter um valor")
        BigDecimal amount,


        String description
) {
}
