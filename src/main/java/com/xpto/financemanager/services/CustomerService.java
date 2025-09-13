package com.xpto.financemanager.services;

import com.xpto.financemanager.dtos.RequestCustomerDto;
import com.xpto.financemanager.dtos.ResponseCustomerDto;
import com.xpto.financemanager.entities.AddressEntity;
import com.xpto.financemanager.entities.CustomerEntity;
import com.xpto.financemanager.enums.ECustomerType;
import com.xpto.financemanager.repositories.AddressRepository;
import com.xpto.financemanager.repositories.CustomerRepository;
import org.springframework.stereotype.Service;

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

    public ResponseCustomerDto createCustomer(RequestCustomerDto dto, ECustomerType customerType) {

        if(customerType == ECustomerType.PF && (dto.cpf() == null || dto.cpf().isEmpty())) {
            throw new IllegalArgumentException("CPF é obrigatório para pessoa física.");
        }

        if(customerType == ECustomerType.PJ && (dto.cnpj() == null || dto.cnpj().isEmpty())) {
            throw new IllegalArgumentException("CNPJ é obrigatório para pessoa jurídica.");
        }

        if(dto.cpf() != null && !dto.cpf().isEmpty() && dto.cnpj() != null && !dto.cnpj().isEmpty()) {
            throw new IllegalArgumentException("Cliente não pode ter CPF e CNPJ ao mesmo tempo.");
        }

        boolean existsCustomer = false;

        if (customerType == ECustomerType.PF) {
            existsCustomer = this.customerRepository.existsByCpf(dto.cpf());
        } else if (customerType == ECustomerType.PJ) {
            existsCustomer = this.customerRepository.existsByCnpj(dto.cnpj());
        }

        if (existsCustomer) {
            throw new IllegalArgumentException("Cliente já existente.");
        }

        CustomerEntity customer = new CustomerEntity(
                dto.name(),
                dto.cpf(),
                dto.cnpj(),
                dto.phone(),
                customerType
        );

        var customerSaved = this.customerRepository.save(customer);

        AddressEntity address = new AddressEntity(
                dto.address().street(),
                dto.address().homeNumber(),
                dto.address().city(),
                dto.address().complement(),
                dto.address().state(),
                dto.address().zipCode(),
                dto.address().uf(),
                customerSaved
        );
        this.addressRepository.save(address);

        this.accountService.registerInitialAccount(customer, dto.account());

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
}
