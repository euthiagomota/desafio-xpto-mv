package com.xpto.financemanager.entities;

import com.xpto.financemanager.enums.CustomerType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "customer")
public class Customer {

    public Customer(
            String name,
            String cpf,
            String cnpj,
            String phone,
            CustomerType customerType
    ) {
        this.name = name;
        this.cpf = cpf;
        this.cnpj = cnpj;
        this.phone = phone;
        this.customerType = customerType;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    @Column(unique = true, nullable = true)
    private String cpf;

    @Column(unique = true, nullable = true)
    private String cnpj;

    private String phone;

    @Enumerated(EnumType.STRING)
    CustomerType customerType;

    private LocalDateTime registerAt;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Account> accounts;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Address address;

    @PrePersist
    protected void onCreate() {
        this.registerAt = LocalDateTime.now();
    }
}
