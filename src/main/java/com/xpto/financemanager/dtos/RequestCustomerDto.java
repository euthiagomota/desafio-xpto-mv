package com.xpto.financemanager.dtos;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RequestCustomerDto {

        @NotBlank(message = "O nome não pode ser vazio")
        @Size(min = 2, max = 150, message = "O nome deve ter entre 2 e 150 caracteres")
        private String name;

        private String cpf;
        private String cnpj;

        @NotBlank(message = "O telefone não pode ser vazio")
        private String phone;

        @Valid
        private RequestAddressDto address;

        @Valid
        private RequestAccountDto account;
}
