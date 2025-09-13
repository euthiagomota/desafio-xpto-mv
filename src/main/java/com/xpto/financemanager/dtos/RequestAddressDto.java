package com.xpto.financemanager.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RequestAddressDto(
        @NotBlank(message = "A rua não pode estar em branco")
        @Size(min = 2, max = 150, message = "A rua deve ter entre 2 e 150 caracteres")
        String street,

        @NotBlank(message = "O número da casa não pode estar em branco")
        String homeNumber,

        @Size(max = 50, message = "O complemento deve ter no máximo 50 caracteres")
        String complement,

        @NotBlank(message = "A cidade não pode estar em branco")
        @Size(min = 2, max = 100, message = "A cidade deve ter entre 2 e 100 caracteres")
        String city,

        @NotBlank(message = "O estado não pode estar em branco")
        @Size(min = 2, max = 100, message = "O estado deve ter entre 2 e 100 caracteres")
        String state,

        @NotBlank(message = "O CEP não pode estar em branco")
        @Pattern(regexp = "\\d{8}", message = "O CEP deve conter exatamente 8 dígitos")
        String zipCode,

        @NotBlank(message = "A UF não pode estar em branco")
        @Pattern(regexp = "[A-Z]{2}", message = "A UF deve conter exatamente 2 letras maiúsculas (ex: SP, RJ, MG)")
        String uf
) {
}
