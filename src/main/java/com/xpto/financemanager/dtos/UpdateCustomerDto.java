package com.xpto.financemanager.dtos;

import jakarta.validation.constraints.Size;

public record UpdateCustomerDto(
        @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 150 caracteres")
        String name,
        String phone
) {
}
