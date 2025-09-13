package com.xpto.financemanager.services;

import com.xpto.financemanager.dtos.RequestAccountDto;
import com.xpto.financemanager.dtos.ResponseAccountDto;
import com.xpto.financemanager.dtos.UpdateAccountDto;
import com.xpto.financemanager.entities.AccountEntity;
import com.xpto.financemanager.entities.CustomerEntity;
import com.xpto.financemanager.exceptions.AccountAlreadyExistsException;
import com.xpto.financemanager.exceptions.NotFoundException;
import com.xpto.financemanager.repositories.AccountRepository;
import com.xpto.financemanager.repositories.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionService transactionService;
    private final CustomerRepository customerRepository;

    public AccountService(
            AccountRepository accountRepository,
            TransactionService transactionService,
            CustomerRepository customerRepository
    ) {
        this.accountRepository = accountRepository;
        this.transactionService = transactionService;
        this.customerRepository = customerRepository;
    }

    public void registerInitialAccount(CustomerEntity customer, RequestAccountDto dto) {
        var existingAccount = accountRepository.
                findByAccountNumberAndBankAndAgency(dto.accountNumber(), dto.bank(), dto.agency());

        if(existingAccount.isPresent()) {
            throw new AccountAlreadyExistsException("Conta já existente no sistema.");
        }

        AccountEntity account = AccountEntity.builder()
                .accountNumber(dto.accountNumber())
                .agency(dto.agency())
                .bank(dto.bank())
                .balance(dto.balance())
                .active(true)
                .customer(customer)
                .build();

       AccountEntity savedAccount = this.accountRepository.save(account);

       this.transactionService.createInitialTransaction(savedAccount, dto.balance());
    }

    public ResponseAccountDto registerAccount(Long customerId, RequestAccountDto dto) {
        var existingAccount = accountRepository.
                findByAccountNumberAndBankAndAgency(dto.accountNumber(), dto.bank(), dto.agency());

        if(existingAccount.isPresent()) {
            throw new AccountAlreadyExistsException("Essa conta já existe em nosso sistema.");
        }

        CustomerEntity customer = this.customerRepository.findById(customerId)
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado."));

        AccountEntity account = AccountEntity.builder()
                .accountNumber(dto.accountNumber())
                .agency(dto.agency())
                .bank(dto.bank())
                .balance(dto.balance())
                .active(true)
                .customer(customer)
                .build();

        var savedAccount = this.accountRepository.save(account);

        return new ResponseAccountDto(
                savedAccount.getId(),
                savedAccount.getAccountNumber(),
                savedAccount.getBank(),
                savedAccount.getAgency(),
                savedAccount.getBalance(),
                savedAccount.getCustomer().getName(),
                savedAccount.getActive()
        );
    }

    public ResponseAccountDto updateAccount(Long accountId, UpdateAccountDto dto) {
        var account = this.accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException("Conta com id " + accountId + " não encontrada"));

        account.setActive(dto.active());
        var accountSaved = this.accountRepository.save(account);

        return new ResponseAccountDto(
                accountSaved.getId(),
                accountSaved.getAccountNumber(),
                accountSaved.getBank(),
                accountSaved.getAgency(),
                accountSaved.getBalance(),
                accountSaved.getCustomer().getName(),
                accountSaved.getActive()
        );
    }
}
