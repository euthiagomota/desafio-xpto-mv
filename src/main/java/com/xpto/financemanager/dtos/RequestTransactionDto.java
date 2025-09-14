package com.xpto.financemanager.dtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class RequestTransactionDto {

        @NotBlank(message = "O número da conta não pode estar em branco")
        @Pattern(regexp = "\\d{4,10}", message = "O número da conta deve conter entre 4 e 10 dígitos")
        private String accountNumber;

        @NotBlank(message = "O banco não pode estar em branco")
        @Size(min = 2, max = 100, message = "O nome do banco deve ter entre 2 e 100 caracteres")
        private String bank;

        @NotBlank(message = "A agência não pode estar em branco")
        @Pattern(regexp = "\\d{3,6}", message = "A agência deve conter entre 3 e 6 dígitos")
        private String agency;

        @NotNull(message = "A transação deve ter um valor")
        private BigDecimal amount;

        private String description;
}