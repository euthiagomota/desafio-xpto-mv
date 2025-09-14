package com.xpto.financemanager.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCustomerDto {

        @Size(min = 2, max = 150, message = "O nome deve ter entre 2 e 150 caracteres")
        private String name;

        private String phone;
}
