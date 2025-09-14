package com.xpto.financemanager.services;

import com.xpto.financemanager.entities.AccountEntity;
import com.xpto.financemanager.entities.CustomerEntity;
import com.xpto.financemanager.repositories.AccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomersReport {
    CustomerService customerService;
    AccountRepository accountRepository;

    public CustomersReport(
            CustomerService customerService,
            AccountRepository accountRepository
    ) {
        this.customerService = customerService;
        this.accountRepository = accountRepository;
    }

    public List<String> execute() {
        List<CustomerEntity> customers = this.customerService.find();

        List<String> messages = new ArrayList<>();

        customers.forEach(customer -> {
            String name = customer.getName();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String registerDate = customer.getRegisterAt().format(formatter);

            List<AccountEntity> accounts = this.accountRepository.findByCustomerId(customer.getId());

            BigDecimal totalBalance = BigDecimal.ZERO;
            for (AccountEntity account : accounts) {
                totalBalance = totalBalance.add(account.getBalance());
            }

            String message = "Cliente: " + name + " - Cliente desde: "+ registerDate + " – Saldo atual: " + String.format("R$ %.2f", totalBalance);
            System.out.println(message);
            messages.add(message);
        });

        return messages;
    }
}
