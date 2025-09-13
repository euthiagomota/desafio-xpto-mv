package com.xpto.financemanager.dtos;

public record RequestAddressDto(
         String street,
         String homeNumber,
         String complement,
         String city,
         String state,
         String zipCode,
         String uf
) {
}
