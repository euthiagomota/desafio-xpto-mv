package com.xpto.financemanager.services;

import com.xpto.financemanager.dtos.RequestTransactionDto;
import com.xpto.financemanager.dtos.ResponseTransactionDto;
import com.xpto.financemanager.entities.Account;
import com.xpto.financemanager.entities.Transaction;
import com.xpto.financemanager.enums.TransactionType;
import com.xpto.financemanager.repositories.AccountRepository;
import com.xpto.financemanager.repositories.TransactionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionService(
            TransactionRepository transactionRepository,
            AccountRepository accountRepository
    ) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    public void createInitialTransaction(Account account, BigDecimal balance) {

        Transaction transaction = new Transaction(
                TransactionType.CREDIT,
                balance,
                "Primeira transação",
                account
        );

        this.transactionRepository.save(transaction);
    }

    public ResponseTransactionDto createTransaction(RequestTransactionDto dto, TransactionType transactionType) {
        Account account = accountRepository
                .findByAccountNumberAndBankAndAgency(dto.getAccountNumber(), dto.getBank(), dto.getAgency())
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada para a movimentação"));

        if (!account.getActive()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Esta conta está desativada.");
        }

        Transaction transaction = new Transaction(
                transactionType,
                dto.getAmount(),
                dto.getDescription(),
                account
        );

        Transaction transactionSaved = this.transactionRepository.save(transaction);

        if (transactionType == TransactionType.CREDIT) {
            account.setBalance(account.getBalance().add(transaction.getAmount()));
        } else if (transactionType == TransactionType.DEBIT) {
            account.setBalance(account.getBalance().subtract(transaction.getAmount()));
        }
        accountRepository.save(account);

        return new ResponseTransactionDto(
                transactionSaved.getId(),
                transactionSaved.getAmount(),
                transactionSaved.getDescription(),
                transactionSaved.getTransactionDate(),
                account.getCustomer().getName(),
                transactionType,
                account.getBank()
        );
    }
}
