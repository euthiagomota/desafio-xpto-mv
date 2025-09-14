package com.xpto.financemanager.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "account")
public class Account {

    public Account(
            String accountNumber,
            String bank,
            String agency,
            BigDecimal balance,
            Customer customer,
            Boolean active
    ) {
        this.accountNumber = accountNumber;
        this.bank = bank;
        this.agency = agency;
        this.balance = balance;
        this.customer = customer;
        this.active = active;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String accountNumber;
    private String bank;
    private String agency;
    private Boolean active;

    @ManyToOne()
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private BigDecimal balance;

    @OneToMany(mappedBy = "account", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Transaction> transactions;
}
