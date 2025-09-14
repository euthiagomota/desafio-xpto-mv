package com.xpto.financemanager.services;

import com.xpto.financemanager.dtos.RequestCustomerDto;
import com.xpto.financemanager.dtos.ResponseCustomerDto;
import com.xpto.financemanager.dtos.UpdateCustomerDto;
import com.xpto.financemanager.entities.Address;
import com.xpto.financemanager.entities.Customer;
import com.xpto.financemanager.enums.CustomerType;
import com.xpto.financemanager.exceptions.NotFoundException;
import com.xpto.financemanager.repositories.AddressRepository;
import com.xpto.financemanager.repositories.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final AccountService accountService;
    private final AddressRepository addressRepository;

    public CustomerService(
            CustomerRepository customerRepository,
            AccountService accountService,
            AddressRepository addressRepository
    ) {
        this.customerRepository = customerRepository;
        this.accountService = accountService;
        this.addressRepository = addressRepository;
    }

    public ResponseCustomerDto createCustomer(RequestCustomerDto dto, CustomerType customerType) {

        if (customerType == CustomerType.PF && (dto.getCpf() == null || dto.getCpf().isEmpty())) {
            throw new IllegalArgumentException("CPF é obrigatório para pessoa física.");
        }

        if (customerType == CustomerType.PJ && (dto.getCnpj() == null || dto.getCnpj().isEmpty())) {
            throw new IllegalArgumentException("CNPJ é obrigatório para pessoa jurídica.");
        }

        if (dto.getCpf() != null && !dto.getCpf().isEmpty() && dto.getCnpj() != null && !dto.getCnpj().isEmpty()) {
            throw new IllegalArgumentException("Cliente não pode ter CPF e CNPJ ao mesmo tempo.");
        }

        boolean existsCustomer = false;

        if (customerType == CustomerType.PF) {
            existsCustomer = this.customerRepository.existsByCpf(dto.getCpf());
        } else if (customerType == CustomerType.PJ) {
            existsCustomer = this.customerRepository.existsByCnpj(dto.getCnpj());
        }

        if (existsCustomer) {
            throw new IllegalArgumentException("Cliente já existente.");
        }

        Customer customer = new Customer(
                dto.getName(),
                dto.getCpf(),
                dto.getCnpj(),
                dto.getPhone(),
                customerType
        );

        Customer customerSaved = this.customerRepository.save(customer);

        Address address = new Address(
                dto.getAddress().getStreet(),
                dto.getAddress().getHomeNumber(),
                dto.getAddress().getCity(),
                dto.getAddress().getComplement(),
                dto.getAddress().getState(),
                dto.getAddress().getZipCode(),
                dto.getAddress().getUf(),
                customerSaved
        );
        this.addressRepository.save(address);

        this.accountService.registerInitialAccount(customer, dto.getAccount());

        return new ResponseCustomerDto(
                customerSaved.getId(),
                customerSaved.getName(),
                customerSaved.getCustomerType(),
                customerSaved.getCpf(),
                customerSaved.getCnpj(),
                customerSaved.getPhone(),
                customerSaved.getRegisterAt()
        );
    }

    public ResponseCustomerDto update(Long id, UpdateCustomerDto dto) {
       Customer customer = this.customerRepository.findById(id)
               .orElseThrow(() -> new NotFoundException("Cliente não encontrado."));

        if (dto.getName() != null && !dto.getName().isEmpty()) {
            customer.setName(dto.getName());
        }

        if (dto.getPhone() != null && !dto.getPhone().isEmpty()) {
            customer.setPhone(dto.getPhone());
        }
      customer = this.customerRepository.save(customer);

        return new ResponseCustomerDto(
                customer.getId(),
                customer.getName(),
                customer.getCustomerType(),
                customer.getCpf(),
                customer.getCnpj(),
                customer.getPhone(),
                customer.getRegisterAt()
        );
    }

    public void delete(Long id) {
        Customer customer = this.customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado."));
        this.customerRepository.delete(customer);
    }

    public List<Customer> find() {
        return this.customerRepository.findAll();
    }
}
