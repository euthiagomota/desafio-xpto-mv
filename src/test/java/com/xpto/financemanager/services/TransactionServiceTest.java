package com.xpto.financemanager.services;

import com.xpto.financemanager.dtos.RequestTransactionDto;
import com.xpto.financemanager.dtos.ResponseTransactionDto;
import com.xpto.financemanager.entities.Account;
import com.xpto.financemanager.entities.Customer;
import com.xpto.financemanager.entities.Transaction;
import com.xpto.financemanager.enums.TransactionType;
import com.xpto.financemanager.repositories.AccountRepository;
import com.xpto.financemanager.repositories.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    private TransactionRepository transactionRepository;
    private AccountRepository accountRepository;
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        transactionRepository = mock(TransactionRepository.class);
        accountRepository = mock(AccountRepository.class);
        transactionService = new TransactionService(transactionRepository, accountRepository);
    }

    @Test
    void shouldCreateInitialTransaction() {
        Account account = new Account();
        account.setBalance(BigDecimal.ZERO);

        transactionService.createInitialTransaction(account, BigDecimal.valueOf(100));

        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(captor.capture());

        Transaction saved = captor.getValue();
        assertEquals(BigDecimal.valueOf(100), saved.getAmount());
        assertEquals("Primeira transação", saved.getDescription());
        assertEquals(TransactionType.CREDIT, saved.getTransactionType());
    }

    @Test
    void shouldCreateCreditTransactionAndIncreaseBalance() {
        Customer customer = new Customer();
        customer.setName("John Doe");

        Account account = new Account();
        account.setAccountNumber("123");
        account.setBank("BankX");
        account.setAgency("0001");
        account.setBalance(BigDecimal.valueOf(500));
        account.setActive(true);
        account.setCustomer(customer);

        RequestTransactionDto dto = new RequestTransactionDto("123", "BankX", "0001", BigDecimal.valueOf(200), "Salary");

        when(accountRepository.findByAccountNumberAndBankAndAgency("123", "BankX", "0001"))
                .thenReturn(Optional.of(account));

        Transaction transaction = new Transaction(TransactionType.CREDIT, dto.getAmount(), dto.getDescription(), account);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        ResponseTransactionDto response = transactionService.createTransaction(dto, TransactionType.CREDIT);

        assertNotNull(response);
        assertEquals(BigDecimal.valueOf(700), account.getBalance());
        assertEquals("John Doe", response.getCustomerName());
        verify(accountRepository).save(account);
    }

    @Test
    void shouldCreateDebitTransactionAndDecreaseBalance() {
        Customer customer = new Customer();
        customer.setName("Alice");

        Account account = new Account();
        account.setAccountNumber("456");
        account.setBank("BankY");
        account.setAgency("0002");
        account.setBalance(BigDecimal.valueOf(1000));
        account.setActive(true);
        account.setCustomer(customer);

        RequestTransactionDto dto = new RequestTransactionDto("456", "BankY", "0002", BigDecimal.valueOf(300), "Shopping");

        when(accountRepository.findByAccountNumberAndBankAndAgency("456", "BankY", "0002"))
                .thenReturn(Optional.of(account));

        Transaction transaction = new Transaction(TransactionType.DEBIT, dto.getAmount(), dto.getDescription(), account);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        ResponseTransactionDto response = transactionService.createTransaction(dto, TransactionType.DEBIT);

        assertNotNull(response);
        assertEquals(BigDecimal.valueOf(700), account.getBalance());
        assertEquals("Alice", response.getCustomerName());
        verify(accountRepository).save(account);
    }

    @Test
    void shouldThrowWhenAccountNotFound() {
        RequestTransactionDto dto = new RequestTransactionDto("999", "BankZ", "0003", BigDecimal.valueOf(50), "Test");

        when(accountRepository.findByAccountNumberAndBankAndAgency("999", "BankZ", "0003"))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> transactionService.createTransaction(dto, TransactionType.CREDIT));
    }

    @Test
    void shouldThrowWhenAccountIsInactive() {
        Account account = new Account();
        account.setActive(false);

        RequestTransactionDto dto = new RequestTransactionDto("123", "BankX", "0001", BigDecimal.valueOf(50), "Test");

        when(accountRepository.findByAccountNumberAndBankAndAgency("123", "BankX", "0001"))
                .thenReturn(Optional.of(account));

        assertThrows(ResponseStatusException.class,
                () -> transactionService.createTransaction(dto, TransactionType.CREDIT));
    }
}
