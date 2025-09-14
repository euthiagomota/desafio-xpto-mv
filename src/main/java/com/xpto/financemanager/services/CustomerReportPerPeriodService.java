package com.xpto.financemanager.services;

import com.xpto.financemanager.entities.AccountEntity;
import com.xpto.financemanager.entities.AddressEntity;
import com.xpto.financemanager.entities.CustomerEntity;
import com.xpto.financemanager.entities.TransactionEntity;
import com.xpto.financemanager.enums.ETransactionType;
import com.xpto.financemanager.exceptions.NotFoundException;
import com.xpto.financemanager.repositories.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class CustomerReportPerPeriodService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;
    private final ReportRepository reportRepository;

    public CustomerReportPerPeriodService(
            TransactionRepository transactionRepository,
            AccountRepository accountRepository,
            CustomerRepository customerRepository,
            AddressRepository addressRepository,
            ReportRepository reportRepository
    ) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.addressRepository = addressRepository;
        this.reportRepository = reportRepository;
    }

    public String generateCustomerReportPerPeriod(Long customerId, LocalDate initialDate, LocalDate finalDate) {
        var customer = this.customerRepository.findById(customerId)
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado"));

        List<AccountEntity> accounts = this.accountRepository.findByCustomerId(customerId);

        List<Long> accountIds = accounts
                .stream()
                .map(AccountEntity::getId)
                .toList();

        LocalDateTime start = initialDate.atTime(0, 0, 0);
        LocalDateTime end = finalDate.atTime(23, 59, 59);

        List<TransactionEntity> transactions = this.transactionRepository
                .findByAccountIdInAndTransactionDateBetween(accountIds, start, end);

        int creditsTransactionsQuantity = (int) transactions.stream()
                .filter(t -> t.getTransactionType() == ETransactionType.CREDIT)
                .count();

        int debitsTransactionsQuantity = (int) transactions.stream()
                .filter(t -> t.getTransactionType() == ETransactionType.DEBIT)
                .count();

        BigDecimal actualValue = accounts.stream().map(AccountEntity::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        String customerName = customer.getName();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dataRegister = customer.getRegisterAt().format(formatter);

        String period = initialDate.format(formatter) + " a " + finalDate.format(formatter);

        AddressEntity addr = this.addressRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new NotFoundException("Endereço não encontrado"));

        String address = addr != null
                ? addr.getStreet() + ", " + addr.getHomeNumber() + ", " +
                (addr.getComplement() != null ? " - " + addr.getComplement() : "") +
                ", " + addr.getCity() + " - " + addr.getUf() + ", " + addr.getZipCode()
                : "Endereço não informado";

        int totalTransactions = transactions.size();
        BigDecimal valorPago = this.calculateCustomerValuePerPeriod(customerId, initialDate, finalDate);

        String relatorio = "Relatório de saldo do cliente " + customerName + "e período: \n" +
                "Período: " + period + "\n" +
                "Cliente: " + customerName + " - Cliente desde: " + dataRegister + ";\n" +
                "Endereço: " + address + ";\n" +
                "Movimentações de crédito: " + creditsTransactionsQuantity + ";\n" +
                "Movimentações de débito: " + debitsTransactionsQuantity + ";\n" +
                "Total de movimentações: " + totalTransactions + ";\n" +
                "Valor pago pelas movimentações: " + String.format("R$ %.2f", valorPago) + "\n" +
                "Saldo atual: " + String.format("R$ %.2f", actualValue);

        System.out.println(relatorio);
        return relatorio;
    }

    private BigDecimal calculateCustomerValuePerPeriod(
            Long customerId, LocalDate initialDate, LocalDate finalDate
    ) {
        CustomerEntity customer = this.customerRepository.findById(customerId)
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado."));

        return this.reportRepository.calculateCustomerValuePerPeriod(customerId,initialDate, finalDate);
    }
}
