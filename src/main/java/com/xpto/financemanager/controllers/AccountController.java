package com.xpto.financemanager.controllers;

import com.xpto.financemanager.dtos.RequestAccountDto;
import com.xpto.financemanager.dtos.ResponseAccountDto;
import com.xpto.financemanager.dtos.ResponseCustomerDto;
import com.xpto.financemanager.dtos.UpdateAccountDto;
import com.xpto.financemanager.services.AccountService;
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
@RequestMapping("account")
@Tag(name = "Account", description = "Gerenciamento de contas de clientes")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Operation(
            summary = "Registra uma nova conta bancária",
            description = "Cria um cliente do tipo Pessoa Física (PF) ou Jurídica (PJ) e retorna as informações criadas.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso",
                            content = @Content(schema = @Schema(implementation = ResponseCustomerDto.class))),
            }
    )
    @PostMapping("/{customerId}")
    public ResponseEntity<ResponseAccountDto> register(
            @Valid @RequestBody RequestAccountDto dto,
            @PathVariable("customerId") Long customerId
    ) {
        ResponseAccountDto response = this.accountService.registerAccount(customerId, dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Atualiza uma conta bancária",
            description = "Atualiza uma conta bancária e retorna as informações.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cliente criado com sucesso",
                            content = @Content(schema = @Schema(implementation = ResponseAccountDto.class))),
            }
    )
    @PatchMapping("/{id}/update")
    public ResponseEntity<ResponseAccountDto> update(
            @Valid  @RequestBody UpdateAccountDto dto,
            @PathVariable("id") Long id
            ) {
        ResponseAccountDto response =  this.accountService.updateAccount(id, dto);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
