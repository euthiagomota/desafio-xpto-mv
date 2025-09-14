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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AccountServiceTest {

    private AccountRepository accountRepository;
    private TransactionService transactionService;
    private CustomerRepository customerRepository;
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        accountRepository = mock(AccountRepository.class);
        transactionService = mock(TransactionService.class);
        customerRepository = mock(CustomerRepository.class);
        accountService = new AccountService(accountRepository, transactionService, customerRepository);
    }

    @Test
    void testRegisterInitialAccount_Success() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Thiago");

        BigDecimal balance = BigDecimal.valueOf(1000);

        RequestAccountDto dto = new RequestAccountDto("12345", "001", "XYZ Bank", balance);

        when(accountRepository.findByAccountNumberAndBankAndAgency(dto.accountNumber(), dto.bank(), dto.agency()))
                .thenReturn(Optional.empty());
        when(accountRepository.save(any(Account.class))).thenAnswer(i -> i.getArgument(0));

        accountService.registerInitialAccount(customer, dto);

        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(captor.capture());
        verify(transactionService).createInitialTransaction(any(Account.class), eq(dto.balance()));

        Account savedAccount = captor.getValue();
        assertEquals(dto.accountNumber(), savedAccount.getAccountNumber());
        assertEquals(dto.agency(), savedAccount.getAgency());
        assertEquals(dto.bank(), savedAccount.getBank());
        assertEquals(dto.balance(), savedAccount.getBalance());
        assertEquals(customer, savedAccount.getCustomer());
        assertTrue(savedAccount.getActive());
    }

    @Test
    void testRegisterInitialAccount_AccountAlreadyExists() {
        BigDecimal balance = BigDecimal.valueOf(1000);
        Customer customer = new Customer();
        RequestAccountDto dto = new RequestAccountDto("12345", "001", "XYZ Bank", balance);
        when(accountRepository.findByAccountNumberAndBankAndAgency(dto.accountNumber(), dto.bank(), dto.agency()))
                .thenReturn(Optional.of(new Account()));

        assertThrows(AccountAlreadyExistsException.class, () ->
                accountService.registerInitialAccount(customer, dto));
    }

    @Test
    void testRegisterAccount_Success() {
        Long customerId = 1L;
        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setName("Thiago");

        BigDecimal balance = BigDecimal.valueOf(1000);

        RequestAccountDto dto = new RequestAccountDto("12345", "001", "XYZ Bank", balance);

        when(accountRepository.findByAccountNumberAndBankAndAgency(dto.accountNumber(), dto.bank(), dto.agency()))
                .thenReturn(Optional.empty());
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(accountRepository.save(any(Account.class))).thenAnswer(i -> i.getArgument(0));

        ResponseAccountDto response = accountService.registerAccount(customerId, dto);

        assertEquals(dto.accountNumber(), response.accountNumber());
        assertEquals(dto.agency(), response.agency());
        assertEquals(dto.bank(), response.bank());
        assertEquals(dto.balance(), response.balance());
        assertEquals(customer.getName(), response.accountOwner());
        assertTrue(response.active());
    }

    @Test
    void testRegisterAccount_CustomerNotFound() {
        BigDecimal balance = BigDecimal.valueOf(1000);
        Long customerId = 1L;
        RequestAccountDto dto = new RequestAccountDto("12345", "001", "XYZ Bank", balance);
        when(accountRepository.findByAccountNumberAndBankAndAgency(dto.accountNumber(), dto.bank(), dto.agency()))
                .thenReturn(Optional.empty());
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> accountService.registerAccount(customerId, dto));
    }

    @Test
    void testUpdateAccount_Success() {
        BigDecimal balance = BigDecimal.valueOf(1000);
        Long accountId = 1L;
        Customer customer = new Customer();
        customer.setName("Thiago");
        Account account = Account.builder()
                .id(accountId)
                .accountNumber("12345")
                .agency("001")
                .bank("XYZ Bank")
                .balance(balance)
                .active(true)
                .customer(customer)
                .build();

        UpdateAccountDto dto = new UpdateAccountDto(false);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenAnswer(i -> i.getArgument(0));

        ResponseAccountDto response = accountService.updateAccount(accountId, dto);

        assertEquals(accountId, response.id());
        assertFalse(response.active());
    }

    @Test
    void testUpdateAccount_NotFound() {
        Long accountId = 1L;
        UpdateAccountDto dto = new UpdateAccountDto(true);

        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> accountService.updateAccount(accountId, dto));
    }
}