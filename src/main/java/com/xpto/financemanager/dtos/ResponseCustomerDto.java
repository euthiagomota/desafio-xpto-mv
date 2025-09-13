package com.xpto.financemanager.dtos;

import com.xpto.financemanager.enuns.ECustomerType;

import java.time.LocalDateTime;

public record ResponseCustomerDto(
        Long id,
        String name,
        ECustomerType customerType,
        String cpf,
        String cnpj,
        String phone,
        LocalDateTime registerAt
) {
}
