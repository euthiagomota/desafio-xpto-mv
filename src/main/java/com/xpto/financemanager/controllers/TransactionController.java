package com.xpto.financemanager.controllers;

import com.xpto.financemanager.dtos.RequestTransactionDto;
import com.xpto.financemanager.dtos.ResponseCustomerDto;
import com.xpto.financemanager.dtos.ResponseTransactionDto;
import com.xpto.financemanager.enums.ETransactionType;
import com.xpto.financemanager.services.TransactionService;
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
@RequestMapping("transaction")
@Tag(name = "Transaction", description = "Gerenciamento de transações")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Operation(
            summary = "Registra uma nova transação",
            description = "Cria uma transação para o cliente, podendo ser do tipo crédito ou débito conforme informado na URL.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Transação criada com sucesso",
                            content = @Content(schema = @Schema(implementation = ResponseTransactionDto.class))),
            }
    )
    @PostMapping("/{transactionType}")
    public ResponseEntity<ResponseTransactionDto> create(
            @Valid @RequestBody RequestTransactionDto dto,
            @PathVariable("transactionType")ETransactionType transactionType
            ) {
        var response = this.transactionService.createTransaction(dto, transactionType);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
