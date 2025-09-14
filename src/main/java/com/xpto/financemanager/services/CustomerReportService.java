package com.xpto.financemanager.services;

import com.xpto.financemanager.entities.Account;
import com.xpto.financemanager.entities.Address;
import com.xpto.financemanager.entities.Customer;
import com.xpto.financemanager.entities.Transaction;
import com.xpto.financemanager.enums.TransactionType;
import com.xpto.financemanager.exceptions.NotFoundException;
import com.xpto.financemanager.repositories.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerReportService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;
    private final ReportRepository reportRepository;

    public CustomerReportService(
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

    public void generateCustomerReport(Long customerId) {
        Customer customer = this.customerRepository.findById(customerId)
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado"));

        List<Account> accounts = this.accountRepository.findByCustomerId(customerId);

        List<Long> accountIds = accounts
                .stream()
                .map(Account::getId)
                .collect(Collectors.toList());

        List<Transaction> transactions = this.transactionRepository.findByAccountIdIn(accountIds);

        Transaction firstTransaction = transactions.stream()
                .min(Comparator.comparing(Transaction::getTransactionDate))
                .orElseThrow(() -> new NotFoundException("Nenhuma transação encontrada."));

        int creditsTransactionsQuantity = (int) transactions.stream()
                .filter(t -> t.getTransactionType() == TransactionType.CREDIT)
                .count();

        int debitsTransactionsQuantity = (int) transactions.stream()
                .filter(t -> t.getTransactionType() == TransactionType.DEBIT)
                .count();

        BigDecimal actualValue = accounts.stream().map(Account::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        String customerName = customer.getName();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String dataRegister = customer.getRegisterAt().format(formatter);

        Address addr = this.addressRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new NotFoundException("Endereço não encontrado"));

        String address = addr != null
                ? addr.getStreet() + ", " + addr.getHomeNumber() + ", " +
                (addr.getComplement() != null ? " - " + addr.getComplement() : "") +
                ", " + addr.getCity() + " - " + addr.getUf() + ", " + addr.getZipCode()
                : "Endereço não informado";

        int totalTransactions = transactions.size();
        BigDecimal valorPago = this.calculateCustomerValue(customerId);
        BigDecimal initialValue = firstTransaction.getAmount();

        String relatorio = "Relatório de saldo do cliente " + customerName + ":\n" +
                "Cliente: " + customerName + " - Cliente desde: " + dataRegister + ";\n" +
                "Endereço: " + address + ";\n" +
                "Movimentações de crédito: " + creditsTransactionsQuantity + ";\n" +
                "Movimentações de débito: " + debitsTransactionsQuantity + ";\n" +
                "Total de movimentações: " + totalTransactions + ";\n" +
                "Valor pago pelas movimentações: " + String.format("R$ %.2f", valorPago) + "\n" +
                "Saldo inicial: " + String.format("R$ %.2f", initialValue) + "\n" +
                "Saldo atual: " + String.format("R$ %.2f", actualValue);

        System.out.println(relatorio);
    }

    private BigDecimal calculateCustomerValue(Long customerId) {
        Customer customer = this.customerRepository.findById(customerId)
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado."));

        return this.reportRepository.calculateCustomerValue(customer.getId());
    }
}
