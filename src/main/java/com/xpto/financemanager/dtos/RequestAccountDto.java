package com.xpto.financemanager.dtos;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record RequestAccountDto(
        @NotBlank(message = "O número da conta não pode estar em branco")
        @Pattern(regexp = "\\d{4,10}", message = "O número da conta deve conter entre 4 e 10 dígitos")
        String accountNumber,

        @NotBlank(message = "O banco não pode estar em branco")
        @Size(min = 2, max = 100, message = "O nome do banco deve ter entre 2 e 100 caracteres")
        String bank,

        @NotBlank(message = "A agência não pode estar em branco")
        @Pattern(regexp = "\\d{3,6}", message = "A agência deve conter entre 3 e 6 dígitos")
        String agency,

        @NotNull(message = "O saldo não pode ser nulo")
        @DecimalMin(value = "0.0", inclusive = true, message = "O saldo deve ser maior ou igual a 0")
        @Digits(integer = 12, fraction = 2, message = "O saldo deve ter no máximo 12 dígitos inteiros e 2 decimais")
        BigDecimal balance
) {}
