package com.xpto.financemanager.controllers;

import com.xpto.financemanager.dtos.RequestCustomerDto;
import com.xpto.financemanager.dtos.ResponseCustomerDto;
import com.xpto.financemanager.dtos.UpdateCustomerDto;
import com.xpto.financemanager.enums.ECustomerType;
import com.xpto.financemanager.services.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
@Tag(name = "Customer", description = "Gerenciamento de clientes")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(
            summary = "Cria um novo cliente",
            description = "Cria um cliente do tipo Pessoa Física (PF) ou Jurídica (PJ) e retorna as informações criadas.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso",
                            content = @Content(schema = @Schema(implementation = ResponseCustomerDto.class))),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos ou CPF/CNPJ já cadastrado",
                            content = @Content(schema = @Schema(example = "{ " +
                                    "\"timestamp\": \"2025-09-12T20:50:00.123+00:00\"," +
                                    " \"status\": 400, \"error\": \"Bad Request\"," +
                                    " \"message\": \"CPF ou CNPJ inválido.\"," +
                                    " \"path\": \"/customer/PF\" }"))),
            }
    )
    @PostMapping("/{customerType}")
    public ResponseEntity<ResponseCustomerDto> create(
            @Valid @RequestBody RequestCustomerDto dto,
            @PathVariable("customerType")ECustomerType customerType
            ) {
        ResponseCustomerDto response = this.customerService.createCustomer(dto, customerType);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Atualizar dados de um cliente",
            description = "Atualiza dados de um cliente e retorna as informações criadas."
    )
    @PatchMapping("/{id}")
    public ResponseEntity<ResponseCustomerDto> update(
            @Valid @RequestBody UpdateCustomerDto dto,
            @PathVariable("id")Long id
    ) {
        ResponseCustomerDto response = this.customerService.update(id, dto);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(
            summary = "Remover um cliente",
            description = "Remove um cliente e seus registros."
    )
    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable("id")Long id
    ) {
        this.customerService.delete(id);
    }

}
