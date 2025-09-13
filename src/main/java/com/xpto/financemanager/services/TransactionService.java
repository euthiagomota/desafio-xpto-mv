package com.xpto.financemanager.services;

import com.xpto.financemanager.dtos.RequestTransactionDto;
import com.xpto.financemanager.dtos.ResponseTransactionDto;
import com.xpto.financemanager.entities.AccountEntity;
import com.xpto.financemanager.entities.TransactionEntity;
import com.xpto.financemanager.enuns.ETransactionType;
import com.xpto.financemanager.repositories.AccountRepository;
import com.xpto.financemanager.repositories.AddressRepository;
import com.xpto.financemanager.repositories.CustomerRepository;
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
            AccountRepository accountRepository,
            CustomerRepository customerRepository,
            AddressRepository addressRepository
    ) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    public void createInitialTransaction(AccountEntity account, BigDecimal balance) {

        TransactionEntity transaction = new TransactionEntity(
                ETransactionType.CREDIT,
                balance,
                "Primeira transação",
                account
        );

        this.transactionRepository.save(transaction);
    }

    public ResponseTransactionDto createTransaction(RequestTransactionDto dto, ETransactionType transactionType) {
        var account = accountRepository
                .findByAccountNumberAndBankAndAgency(dto.accountNumber(), dto.bank(), dto.agency())
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada para a movimentação"));

        if(!account.getActive()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Esta conta está desativada.");
        }

        TransactionEntity transaction = new TransactionEntity(
                transactionType,
                dto.amount(),
                dto.description(),
                account
        );

        var transactionSaved = this.transactionRepository.save(transaction);

        if (transactionType == ETransactionType.CREDIT) {
            account.setBalance(account.getBalance().add(transaction.getAmount()));
        } else if (transactionType == ETransactionType.DEBIT) {
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
