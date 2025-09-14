package com.xpto.financemanager.services;

import com.xpto.financemanager.dtos.*;
import com.xpto.financemanager.entities.Address;
import com.xpto.financemanager.entities.Customer;
import com.xpto.financemanager.enums.CustomerType;
import com.xpto.financemanager.exceptions.NotFoundException;
import com.xpto.financemanager.repositories.AddressRepository;
import com.xpto.financemanager.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AccountService accountService;

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private CustomerService customerService;

    private RequestCustomerDto requestDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        BigDecimal balance = BigDecimal.valueOf(1000);

        requestDto = new RequestCustomerDto(
                "João Silva",
                "12345678901", // cpf
                null, // cnpj
                "11999999999",
                new RequestAddressDto(
                        "Rua A", "123", "Apto 10", "São Paulo",
                        "SP", "01000-000", "SP"
                ),
                new RequestAccountDto("12345-6", "Banco XPTO", "0001", balance)
        );
    }

    @Test
    void shouldCreateIndividualCustomerSuccessfully() {
        when(customerRepository.existsByCpf("12345678901")).thenReturn(false);
        when(customerRepository.save(any(Customer.class)))
                .thenAnswer(invocation -> {
                    Customer c = invocation.getArgument(0);
                    c.setId(1L);
                    return c;
                });
        when(addressRepository.save(any(Address.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseCustomerDto response = customerService.createCustomer(requestDto, CustomerType.PF);

        assertNotNull(response);
        assertEquals("João Silva", response.getName());
        assertEquals("12345678901", response.getCpf());
        verify(customerRepository, times(1)).save(any(Customer.class));
        verify(accountService, times(1)).registerInitialAccount(any(Customer.class), any());
    }

    @Test
    void shouldNotCreateIndividualCustomerWithoutCpf() {
        RequestCustomerDto dtoSemCpf = new RequestCustomerDto(
                "João Silva", null, null, "11999999999",
                requestDto.getAddress(), requestDto.getAccount()
        );

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> customerService.createCustomer(dtoSemCpf, CustomerType.PF));

        assertEquals("CPF é obrigatório para pessoa física.", ex.getMessage());
    }

    @Test
    void shouldNotCreateCompanyCustomerWithoutCnpj() {
        RequestCustomerDto dtoSemCnpj = new RequestCustomerDto(
                "Empresa X", null, null, "1133334444",
                requestDto.getAddress(), requestDto.getAccount()
        );

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> customerService.createCustomer(dtoSemCnpj, CustomerType.PJ));

        assertEquals("CNPJ é obrigatório para pessoa jurídica.", ex.getMessage());
    }

    @Test
    void shouldNotCreateCustomerWithBothCpfAndCnpj() {
        RequestCustomerDto dtoComCpfECnpj = new RequestCustomerDto(
                "Maria", "12345678901", "11111111000111", "11988887777",
                requestDto.getAddress(), requestDto.getAccount()
        );

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> customerService.createCustomer(dtoComCpfECnpj, CustomerType.PF));

        assertEquals("Cliente não pode ter CPF e CNPJ ao mesmo tempo.", ex.getMessage());
    }

    @Test
    void shouldUpdateCustomer() {
        Customer clienteExistente = new Customer("Ana", "123", null, "1111", CustomerType.PF);
        clienteExistente.setId(10L);

        when(customerRepository.findById(10L)).thenReturn(Optional.of(clienteExistente));
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UpdateCustomerDto updateDto = new UpdateCustomerDto("Ana Maria", "2222");

        ResponseCustomerDto response = customerService.update(10L, updateDto);

        assertEquals("Ana Maria", response.getName());
        assertEquals("2222", response.getPhone());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentCustomer() {
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> customerService.update(99L, new UpdateCustomerDto("Teste", "9999")));
    }

    @Test
    void shouldDeleteCustomer() {
        Customer cliente = new Customer("Carlos", "123", null, "111", CustomerType.PF);
        cliente.setId(1L);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(cliente));

        customerService.delete(1L);

        verify(customerRepository, times(1)).delete(cliente);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentCustomer() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> customerService.delete(1L));
    }
}

