package com.xpto.financemanager.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RequestCustomerDto(
        @NotBlank(message = "O nome não pode ser vazío")
        @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 150 caracteres")
        String name,

        String cpf,

        String cnpj,

        @NotBlank(message = "O telefone não pode ser vazio")
        String phone,

        @Valid
        RequestAddressDto address,

        @Valid
        RequestAccountDto account
) {
}
