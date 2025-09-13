package com.xpto.financemanager.controllers;

import com.xpto.financemanager.dtos.RequestTransactionDto;
import com.xpto.financemanager.dtos.ResponseTransactionDto;
import com.xpto.financemanager.enums.ETransactionType;
import com.xpto.financemanager.services.TransactionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("transaction")
@Tag(name = "Transaction")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/{transactionType}")
    public ResponseEntity<ResponseTransactionDto> create(
            @RequestBody RequestTransactionDto dto,
            @PathVariable("transactionType")ETransactionType transactionType
            ) {
        var response = this.transactionService.createTransaction(dto, transactionType);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
