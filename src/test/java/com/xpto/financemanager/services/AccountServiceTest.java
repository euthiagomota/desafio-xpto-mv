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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

class AccountServiceTest {

    private AccountRepository accountRepository;
    private TransactionService transactionService;
    private CustomerRepository customerRepository;
    private AccountService accountService;

    private Customer customer;
    private RequestAccountDto requestAccountDto;

    @BeforeEach
    void setUp() {
        accountRepository = mock(AccountRepository.class);
        transactionService = mock(TransactionService.class);
        customerRepository = mock(CustomerRepository.class);
        accountService = new AccountService(accountRepository, transactionService, customerRepository);

        customer = new Customer();
        customer.setId(1L);
        customer.setName("Thiago");

        requestAccountDto = new RequestAccountDto("12345", "001", "XYZ Bank", BigDecimal.valueOf(1000));
    }

    @Nested
    class RegisterInitialAccountTests {

        @Test
        void shouldRegisterInitialAccountSuccessfully() {
            when(accountRepository.findByAccountNumberAndBankAndAgency(
                    requestAccountDto.getAccountNumber(),
                    requestAccountDto.getBank(),
                    requestAccountDto.getAgency()))
                    .thenReturn(Optional.empty());

            when(accountRepository.save(any(Account.class))).thenAnswer(i -> i.getArgument(0));

            accountService.registerInitialAccount(customer, requestAccountDto);

            ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
            verify(accountRepository).save(captor.capture());
            verify(transactionService).createInitialTransaction(any(Account.class), eq(requestAccountDto.getBalance()));

            Account savedAccount = captor.getValue();
            assertEquals(requestAccountDto.getAccountNumber(), savedAccount.getAccountNumber());
            assertEquals(requestAccountDto.getAgency(), savedAccount.getAgency());
            assertEquals(requestAccountDto.getBank(), savedAccount.getBank());
            assertEquals(requestAccountDto.getBalance(), savedAccount.getBalance());
            assertEquals(customer, savedAccount.getCustomer());
            assertTrue(savedAccount.getActive());
        }

        @Test
        void shouldThrowExceptionWhenAccountAlreadyExists() {
            when(accountRepository.findByAccountNumberAndBankAndAgency(
                    requestAccountDto.getAccountNumber(),
                    requestAccountDto.getBank(),
                    requestAccountDto.getAgency()))
                    .thenReturn(Optional.of(new Account()));

            assertThrows(AccountAlreadyExistsException.class, () ->
                    accountService.registerInitialAccount(customer, requestAccountDto));
        }
    }

    @Nested
    class RegisterAccountTests {

        @Test
        void shouldRegisterAccountSuccessfully() {
            Long customerId = customer.getId();

            when(accountRepository.findByAccountNumberAndBankAndAgency(
                    requestAccountDto.getAccountNumber(),
                    requestAccountDto.getBank(),
                    requestAccountDto.getAgency()))
                    .thenReturn(Optional.empty());

            when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
            when(accountRepository.save(any(Account.class))).thenAnswer(i -> i.getArgument(0));

            ResponseAccountDto response = accountService.registerAccount(customerId, requestAccountDto);

            assertEquals(requestAccountDto.getAccountNumber(), response.getAccountNumber());
            assertEquals(requestAccountDto.getAgency(), response.getAgency());
            assertEquals(requestAccountDto.getBank(), response.getBank());
            assertEquals(requestAccountDto.getBalance(), response.getBalance());
            assertEquals(customer.getName(), response.getAccountOwner());
            assertTrue(response.getActive());
        }

        @Test
        void shouldThrowNotFoundWhenCustomerDoesNotExist() {
            Long customerId = customer.getId();

            when(accountRepository.findByAccountNumberAndBankAndAgency(
                    requestAccountDto.getAccountNumber(),
                    requestAccountDto.getBank(),
                    requestAccountDto.getAgency()))
                    .thenReturn(Optional.empty());

            when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> accountService.registerAccount(customerId, requestAccountDto));
        }
    }

    @Nested
    class UpdateAccountTests {

        @Test
        void shouldUpdateAccountSuccessfully() {
            Long accountId = 1L;
            Account account = Account.builder()
                    .id(accountId)
                    .accountNumber("12345")
                    .agency("001")
                    .bank("XYZ Bank")
                    .balance(BigDecimal.valueOf(1000))
                    .active(true)
                    .customer(customer)
                    .build();

            UpdateAccountDto dto = new UpdateAccountDto(false);

            when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
            when(accountRepository.save(any(Account.class))).thenAnswer(i -> i.getArgument(0));

            ResponseAccountDto response = accountService.updateAccount(accountId, dto);

            assertEquals(accountId, response.getId());
            assertFalse(response.getActive());
        }

        @Test
        void shouldThrowNotFoundWhenAccountDoesNotExist() {
            Long accountId = 1L;
            UpdateAccountDto dto = new UpdateAccountDto(true);

            when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> accountService.updateAccount(accountId, dto));
        }
    }
}