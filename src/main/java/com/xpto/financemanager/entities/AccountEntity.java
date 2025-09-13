package com.xpto.financemanager.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "account")
public class AccountEntity {

    public AccountEntity(
            String accountNumber,
            String bank,
            String agency,
            BigDecimal balance,
            CustomerEntity customer,
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountNumber;
    private String bank;
    private String agency;
    private Boolean active;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private CustomerEntity customer;

    private BigDecimal balance;

    @OneToMany(mappedBy = "account")
    private List<TransactionEntity> transactions;
}
