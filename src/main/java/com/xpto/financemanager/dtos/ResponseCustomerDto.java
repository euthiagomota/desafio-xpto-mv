package com.xpto.financemanager.dtos;

import com.xpto.financemanager.enums.CustomerType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseCustomerDto {

    private Long id;
    private String name;
    private CustomerType customerType;
    private String cpf;
    private String cnpj;
    private String phone;
    private LocalDateTime registerAt;
}
