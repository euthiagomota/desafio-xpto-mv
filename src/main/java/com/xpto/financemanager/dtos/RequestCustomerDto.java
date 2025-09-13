package com.xpto.financemanager.dtos;

public record RequestCustomerDto(
    String name,
    String cpf,
    String cnpj,
    String phone,
    RequestAddressDto address,
    RequestAccountDto account
) {
}
