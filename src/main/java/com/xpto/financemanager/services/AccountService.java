package com.xpto.financemanager.services;

import com.xpto.financemanager.dtos.RequestAccountDto;
import com.xpto.financemanager.dtos.ResponseAccountDto;
import com.xpto.financemanager.dtos.UpdateAccountDto;
import com.xpto.financemanager.entities.Account;
import com.xpto.financemanager.entities.Customer;
import com.xpto.financemanager.exceptions.AccountAlreadyExistsException;
import com.xpto.financemanager.exceptions.NotFoundException;
import com.xpto.financemanager.repositories.AccountRepository;
import com.xpto.financemanager.repositories.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public void registerInitialAccount(Customer customer, RequestAccountDto dto) {
        Optional<Account> existingAccount = accountRepository.
                findByAccountNumberAndBankAndAgency(dto.getAccountNumber(), dto.getBank(), dto.getAgency());

        if(existingAccount.isPresent()) {
            throw new AccountAlreadyExistsException("Conta já existente no sistema.");
        }

        Account account = Account.builder()
                .accountNumber(dto.getAccountNumber())
                .agency(dto.getAgency())
                .bank(dto.getBank())
                .balance(dto.getBalance())
                .active(true)
                .customer(customer)
                .build();

       Account savedAccount = this.accountRepository.save(account);

       this.transactionService.createInitialTransaction(savedAccount, dto.getBalance());
    }

    public ResponseAccountDto registerAccount(Long customerId, RequestAccountDto dto) {
        Optional<Account> existingAccount = accountRepository.
                findByAccountNumberAndBankAndAgency(dto.getAccountNumber(), dto.getBank(), dto.getAgency());

        if(existingAccount.isPresent()) {
            throw new AccountAlreadyExistsException("Essa conta já existe em nosso sistema.");
        }

        Customer customer = this.customerRepository.findById(customerId)
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado."));

        Account account = Account.builder()
                .accountNumber(dto.getAccountNumber())
                .agency(dto.getAgency())
                .bank(dto.getBank())
                .balance(dto.getBalance())
                .active(true)
                .customer(customer)
                .build();

        Account savedAccount = this.accountRepository.save(account);

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
        Account account = this.accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException("Conta com id " + accountId + " não encontrada"));

        account.setActive(dto.getActive());
        Account accountSaved = this.accountRepository.save(account);

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
